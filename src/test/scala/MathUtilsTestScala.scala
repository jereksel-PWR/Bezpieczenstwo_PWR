import org.scalatest.{FlatSpec, Matchers}
import pl.andrzejressel.bezpieczenstwo.lista4.MathUtils

class MathUtilsTestScala extends FlatSpec with Matchers {

  "13" must "be prime" in {
    MathUtils.isPrime(13) shouldBe true
  }

  "17" must "be prime" in {
    MathUtils.isPrime(17) shouldBe true
  }

  "23" must "be prime" in {
    MathUtils.isPrime(23) shouldBe true
  }

  "24" must "not be prime" in {
    MathUtils.isPrime(24) shouldBe false
  }

}
