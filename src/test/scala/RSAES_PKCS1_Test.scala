import java.security.SecureRandom

import org.scalatest.{FlatSpec, Matchers}
import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.RSA

class RSAES_PKCS1_Test extends FlatSpec with Matchers {

  "Given message" must "be the same after encryption & decryption (PKCS)" in {

    val rsa = RSA.gen(256, 4)

    for (i <- 1 to 1000) {

      val message = "Test message".getBytes("ASCII")

      val decrypted = rsa.RSAES_PKCS1_V1_5_ENCRYPT(message)

      rsa.RSAES_PKCS1_V1_5_DECRYPT(decrypted) shouldBe message
    }
  }

  "Random bytes" must "be the same after encryption & decryption (PKCS)" in {

    val rsa = RSA.gen(256, 4)

    val secRand = new SecureRandom()

    for (i <- 1 to 1000) {

      val bytes: Array[Byte] = Array.fill(60) {0x0}

      secRand.nextBytes(bytes)

      val decrypted = rsa.RSAES_PKCS1_V1_5_ENCRYPT(bytes)

      rsa.RSAES_PKCS1_V1_5_DECRYPT(decrypted) shouldBe bytes
    }
  }

}
