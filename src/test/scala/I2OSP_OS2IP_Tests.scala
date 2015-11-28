import java.security.SecureRandom

import org.scalatest.{Matchers, FlatSpec}
import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.RSA

class I2OSP_OS2IP_Tests extends FlatSpec with Matchers {

  "big array of random bytes" must "be the same after I2OSP/OS2IP" in {

    for (i <- 1 to 1000) {

      val secRand = new SecureRandom

      val bytes = Stream.range(1, 200).map(e => secRand.nextInt().toByte).toList

      val array = RSA.I2OSP(RSA.OS2IP(bytes), bytes.length).toList

      array shouldBe bytes


    }

  }


  "big random number" must "be the same after I2OSP/OS2IP" in {

    for (i <- 1 to 1000) {

      val secRand = new SecureRandom

      val number = BigInt(256, secRand)

      val number2 = RSA.OS2IP(RSA.I2OSP(number, 256))

      number shouldBe number2

    }

  }


}
