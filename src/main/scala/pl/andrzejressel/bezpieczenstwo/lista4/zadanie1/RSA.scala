package pl.andrzejressel.bezpieczenstwo.lista4.zadanie1

import pl.andrzejressel.bezpieczenstwo.lista4.zadanie2.PR

object RSA {

  private def lcm(a: BigInt, b: BigInt): BigInt = {
    a * (b / a.gcd(b))
  }

  def gen(keySize: Int, keyNumber: Int): (PrivKey, PubKey) = {

    val numbers = PR.gimmePrimes(Runtime.getRuntime.availableProcessors() * 2, keySize, keyNumber)

    val n = numbers.foldLeft(BigInt(1))((a, b) => a * b)

    val euler = numbers.foldLeft(BigInt(1))((a, b) => lcm(a, b - BigInt(1)))

    val e = BigInt(Stream.from(5).dropWhile(e => BigInt(e).gcd(euler) != BigInt(1)).head)

    val d = e.modInverse(euler)

    val Rs = numbers.foldLeft(List[BigInt](BigInt(1)))((a, b) => a.::(b * a.head)).reverse
    val Ts = numbers.indices.map(e => Rs(e).modInverse(numbers(e))).toList

    val Ds = numbers.map(a => e.modInverse(a - BigInt(1)))

    (new PrivKey(n, d, numbers, Ds, Ts), new PubKey(e, n))
  }

  def enc(message: BigInt, pubKey: PubKey): BigInt = {
    message.modPow(pubKey.e, pubKey.n)
  }

  def decStandard(ciphertext: BigInt, privkey: PrivKey): BigInt = {
    ciphertext.modPow(privkey.d, privkey.n)
  }

  def decCRT(ciphertext: BigInt, privkey: PrivKey): BigInt = {

    val ds = privkey.ds

    //To można zrobić wielowątkowo
    val ms = privkey.ds.indices.map(e => ciphertext.modPow(privkey.ds(e), privkey.rs(e))).toList

    val numbers = privkey.rs
    val ts = privkey.ts
    val rs = numbers.foldLeft(List[BigInt](BigInt(1)))((a, b) => a.::(b * a.head)).reverse
    val n = privkey.n

    val odszyfrowane = numbers.indices.foldLeft(BigInt(0))((m, b) => {
      val h = (ms(b) - m) * ts(b).mod(numbers(b))
      m + rs(b) * h
    }) % n

    odszyfrowane
  }

}
