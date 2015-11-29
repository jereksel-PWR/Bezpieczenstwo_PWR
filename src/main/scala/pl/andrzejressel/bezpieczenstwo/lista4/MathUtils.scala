package pl.andrzejressel.bezpieczenstwo.lista4

import java.math.BigInteger
import java.security.SecureRandom
import java.util.Random

import scala.math.BigInt

object MathUtils {

  def isPrime(n: BigInt): Boolean = {
    isPrime(n, 30)
  }

  /**
   * Rabin-Miller Test
   * http://mathworld.wolfram.com/Rabin-MillerStrongPseudoprimeTest.html
   * https://pl.wikipedia.org/wiki/Test_Millera-Rabina
   */
  def isPrime(n: BigInt, numberOfTests: Int): Boolean = {

    var d = n - 1

    var bigInteger: (BigInt, BigInt) = null

    var s: Int = 0

    do {
      bigInteger = d /% 2
      d = bigInteger._1
      s += 1
    } while (bigInteger._2 == BigInt(0))

    s -= 1

    d = (d * 2) + 1


    Stream.range(0, numberOfTests).forall {

      var a = BigInt(new BigInteger(n.bitLength - 1, new Random()))

      while (a == BigInt(0)) {
        a = BigInt(new BigInteger(n.bitLength - 1, new Random()))
      }

      val ad = a.modPow(d, n).mod(n)

      val test1 = ad == BigInt(1)

      return test1 || Stream.range(0, s).exists(r => {
        val exponent = (BigInt(2) pow r) * d
        val a2rd = a.modPow(exponent, n).mod(n)
        a2rd == (n - 1)
      })

    }
  }

  def randomNumber(bits: Int): BigInt = {
    val secRand: SecureRandom = new SecureRandom

    // 1 + [n-2 losowe bity] + 1 = n bitowa liczba nieparzysta
    BigInt(2).pow(bits) + (BigInt(bits - 2, secRand) << 1) + BigInt(1)
  }


}
