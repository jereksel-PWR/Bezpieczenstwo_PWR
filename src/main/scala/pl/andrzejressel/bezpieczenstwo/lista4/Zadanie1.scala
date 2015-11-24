package pl.andrzejressel.bezpieczenstwo.lista4

import pl.andrzejressel.bezpieczenstwo.lista4.zadanie2.PR

/**
 * Żródła:
 * https://pl.wikipedia.org/wiki/RSA_(kryptografia)#Opis_algorytmu.5B1.5D
 * https://www.emc.com/collateral/white-papers/h11300-pkcs-1v2-2-rsa-cryptography-standard-wp.pdf
 * http://www.di-mgt.com.au/crt_rsa.html
 */
object Zadanie1 extends App {

  def lcm(a: BigInt, b: BigInt): BigInt = {
    a * (b / a.gcd(b))
  }


  def standardowo(wiadomosc: BigInt, liczby: List[BigInt]) = {

    val superTajnaWiadomosc = wiadomosc

    val numbers = liczby

    val n = numbers.foldLeft(BigInt(1))((a, b) => a * b)

    val euler = numbers.foldLeft(BigInt(1))((a, b) => lcm(a, b - BigInt(1)))

    val e = BigInt(Stream.from(5).dropWhile(e => BigInt(e).gcd(euler) != BigInt(1)).head)

    //Wiadomość
    val c = superTajnaWiadomosc.modPow(e, n)

    //Klucz prywatny
    val d = e.modInverse(euler)


    //STANDARD
    val odszyfrowane = c.modPow(d, n)


  }

  def ctr(wiadomosc: BigInt, liczby: List[BigInt]) = {

    val superTajnaWiadomosc = wiadomosc

    val numbers = liczby

    val n = numbers.foldLeft(BigInt(1))((a, b) => a * b)

    val euler = numbers.foldLeft(BigInt(1))((a, b) => lcm(a, b - BigInt(1)))

    val e = BigInt(Stream.from(5).dropWhile(e => BigInt(e).gcd(euler) != BigInt(1)).head)

    //Wiadomość
    val c = superTajnaWiadomosc.modPow(e, n)


    //(r_i, d_i, t_1)
    val Rs = numbers.foldLeft(List[BigInt](BigInt(1)))((a, b) => a.::(b * a.head)).reverse
    val Ds = numbers.map(a => e.modInverse(a - BigInt(1)))
    val Ts = numbers.indices.map(e => Rs(e).modInverse(numbers(e)))
    val Ms = numbers.indices.map(e => c.modPow(Ds(e), numbers(e)))


    val odszyfrowane = numbers.indices.foldLeft(BigInt(0))((m, b) => {
      val h = (Ms(b) - m) * Ts(b).mod(numbers(b))
      m + Rs(b) * h
    }) % n


  }

  val liczby = PR.gimmePrimes(Runtime.getRuntime.availableProcessors() * 2, 256, 20)

  println("Rozpoczęcie szyfrowania")

  val wiadomosc = BigInt(123123123)

  var t0 = System.nanoTime()

  Range.apply(1, 10).foreach(e => {
    println(e)
    standardowo(wiadomosc, liczby)
  })

  var t1 = System.nanoTime()

  println(s"Czas: ${(t1 - t0) / 1000000000.0}s")

  t0 = System.nanoTime()

  Range.apply(1, 10).foreach(e => {
    println(e)
    ctr(wiadomosc, liczby)
  })

  t1 = System.nanoTime()

  println(s"Czas: ${(t1 - t0) / 1000000000.0}s")


}
