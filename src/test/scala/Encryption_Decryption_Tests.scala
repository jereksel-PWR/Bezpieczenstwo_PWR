import java.security.SecureRandom

import org.scalatest.{FlatSpec, Matchers}
import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.RSA

class Encryption_Decryption_Tests extends FlatSpec with Matchers {

  "Random bytes" must "be the same after encryption & decryption" in {

    val secRand = new SecureRandom()

    for (i <- 1 to 100) {

      val rsa = RSA.gen(256, 4)

      val testInt = BigInt(128, secRand)

      rsa.decCRT(rsa.enc(testInt)) shouldBe testInt

    }
  }

  "Random bytes" must "be decrypted the same in \"standard\" and \"CRT\" mode" in {

    val secRand = new SecureRandom()

    for (i <- 1 to 100) {

      val rsa = RSA.gen(256, 4)

      val testInt = BigInt(256, secRand)
      val enc = rsa.enc(testInt)

      val decSt = rsa.decStandard(enc)
      val decCRT = rsa.decCRT(enc)

      decSt shouldBe decCRT

    }

  }

}
