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

  //Na wszelki wypadek

  "1010 + 1000" must "be 10101000" in {
    MathUtils.combineBigInts(List(BigInt("1010", 2), BigInt("1000", 2))) == BigInt("10101000", 2)
  }

  "0001 + 1000" must "be 11000" in {
    MathUtils.combineBigInts(List(BigInt("0001", 2), BigInt("1000", 2))) == BigInt("11000", 2)
  }

  "0000 + 1000" must "be 1000" in {
    MathUtils.combineBigInts(List(BigInt("0000", 2), BigInt("1000", 2))) == BigInt("1000", 2)
  }

}
