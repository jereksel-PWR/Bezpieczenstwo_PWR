package pl.andrzejressel.bezpieczenstwo.lista4.zadanie1

import java.security.SecureRandom

import pl.andrzejressel.bezpieczenstwo.lista4.zadanie2.PR

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class PrivKey(val n: BigInt, val d: BigInt, val rs: List[BigInt], val ds: List[BigInt], val ts: List[BigInt])

class PubKey(val e: BigInt, val n: BigInt)

/**
 * Żródła:
 * https://pl.wikipedia.org/wiki/RSA_(kryptografia)#Opis_algorytmu.5B1.5D
 * https://www.emc.com/collateral/white-papers/h11300-pkcs-1v2-2-rsa-cryptography-standard-wp.pdf
 * http://www.di-mgt.com.au/crt_rsa.html
 */
class RSA(val privkey: PrivKey, val pubKey: PubKey) {

  def enc(message: BigInt): BigInt = {
    message.modPow(pubKey.e, pubKey.n)
  }

  def decStandard(ciphertext: BigInt): BigInt = {
    ciphertext.modPow(privkey.d, privkey.n).mod(privkey.n)
  }

  def decCRT(ciphertext: BigInt): BigInt = {

    val ds = privkey.ds

    val myFutureList = Future.traverse(ds.indices.map(BigInt(_)))(e => Future(ciphertext.modPow(privkey.ds(e.toInt), privkey.rs(e.toInt))))

    val ms = Await.result(myFutureList, Duration.Inf)

    val numbers = privkey.rs
    val ts = privkey.ts
    val rs = numbers.foldLeft(List[BigInt](BigInt(1)))((a, b) => a.::(b * a.head)).reverse
    val n = privkey.n

    val odszyfrowane = numbers.indices.foldLeft(BigInt(0))((m, b) => {
      val h = (ms(b) - m) * ts(b).mod(numbers(b))
      m + rs(b) * h
    }).mod(n)

    odszyfrowane
  }

  def secRand = new SecureRandom()


  def nonZeroBytes = Stream
    .continually({
      secRand.nextInt().toByte
    })
    .filter(_ != 0)


  def RSAES_PKCS1_V1_5_ENCRYPT(message: Array[Byte]): Array[Byte] = {

    val mLen = message.length
    //+1 na wszelki wypadek
    var k = (pubKey.n.bitLength / 8) + 1

    if (pubKey.n.bitLength % 8 == 0) {
      k = k - 1
    }

    if (mLen > k - 11) {
      throw new RuntimeException("Wiadomość za długa")
    }

    var EM = List[Byte](0x00, 0x02)

    var i = 0

    val ps = nonZeroBytes.take(k - mLen - 3).toList

    EM ++= ps

    EM ++= List[Byte](0x00)
    EM ++= message

    val m = RSA.OS2IP(EM)

    val c = enc(m)

    val C = RSA.I2OSP(c, k)

    C
  }

  def RSAES_PKCS1_V1_5_DECRYPT(C: Array[Byte]): Array[Byte] = {

    var k = (privkey.n.bitLength / 8) + 1

    if (privkey.n.bitLength % 8 == 0) {
      k = k - 1
    }

    if (C.length != k || C.length < 11) {
      throw new RuntimeException("Decryption error")
    }

    val c = RSA.OS2IP(C)

    val m = decCRT(c)
    val m1 = decStandard(c)

    val EM = RSA.I2OSP(m, k)

    if (EM(0) != 0 || EM(1) != 2) {
      throw new RuntimeException("Decryption error")
    }

    val EMList = EM.toList

    val M = EMList.drop(2).dropWhile(e => e != 0).drop(1)

    M.toArray
  }

}


object RSA {

  def OS2IP(X: List[Byte]): BigInt = {
    OS2IP(X.toArray)
  }

  def OS2IP(X: Array[Byte]): BigInt = {
    //RSAUtilsJava.OS2IP(X)
    // BigInt(X.reverse)

    Stream.range(0, X.length).foldLeft(BigInt(0))((a, b) => {
      a + (BigInt(X(b) & 0xff) * BigInt(256).pow(X.length - (b + 1)))
      // a + (BigInt(X(b) & 0xff) * BigInt(256).pow(b + 1))
    })


  }

  def I2OSP(x: BigInt, xLen: Int): Array[Byte] = {


    Stream.range(1, xLen + 1).foldLeft((List[Byte](), x))((pair, i) => {
      val bigData = x./%(BigInt(256).pow(xLen - i))
      (pair._1 ++ List[Byte](bigData._1.byteValue()), bigData._2)
    })._1.toArray


    /*
        if (x > BigInt(256).pow(xLen)) {
          throw new RuntimeException("Integer too large")
        }

        var a = x.toByteArray.reverse

        while (a.length < xLen) {
          a = a.++(List[Byte](0x00))
        }
    */
    // a.reverse

    /*
    var byteArray = List[Byte]()

    Stream.range(0, xLen).foldLeft((List[Byte](), x))((a, i) => {

      val list = a._1
      val bigInt = a._2

      val doListy = (bigInt % 256).toByte

      (list ++ List[Byte](doListy), bigInt >> 8)
    }
    )._1.toArray

*/

    // RSAUtilsJava.I2OSP(x.underlying(), xLen)
  }


  private def lcm(a: BigInt, b: BigInt): BigInt = {
    a * (b / a.gcd(b))
  }

  def gen(keySize: Int, keyNumber: Int): RSA = {

    val numbers = PR.getPrimes(Runtime.getRuntime.availableProcessors() * 2, keySize, keyNumber)

    val n = numbers.foldLeft(BigInt(1))((a, b) => a * b)

    val euler = numbers.foldLeft(BigInt(1))((a, b) => lcm(a, b - BigInt(1)))

    val e = BigInt(Stream.from(5).dropWhile(e => BigInt(e).gcd(euler) != BigInt(1)).head)

    val d = e.modInverse(euler)

    val Rs = numbers.foldLeft(List[BigInt](BigInt(1)))((a, b) => a.::(b * a.head)).reverse
    val Ts = numbers.indices.map(e => Rs(e).modInverse(numbers(e))).toList

    val Ds = numbers.map(a => e.modInverse(a - BigInt(1)))

    new RSA(new PrivKey(n, d, numbers, Ds, Ts), new PubKey(e, n))
  }


}
