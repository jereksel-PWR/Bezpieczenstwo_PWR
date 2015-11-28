import java.security.SecureRandom

import org.scalatest.{Matchers, FlatSpec}
import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.RSA

class Encryption_Decryption_Tests extends FlatSpec with Matchers {

  "Random bytes" must "be the same after encryption & decryption" in {

    val secRand = new SecureRandom()

    for (i <- 1 to 100) {

      val rsa = RSA.gen(256, 4)

      val testInt = BigInt(128, secRand)

      rsa.decStandard(rsa.enc(testInt)) shouldBe testInt

    }
  }


}
