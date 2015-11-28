package pl.andrzejressel.bezpieczenstwo.lista4

import java.io.File

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.apache.commons.io.FileUtils
import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.{PrivKey, PubKey, RSA}

object Main {

  def printHelp(): Unit = {
    println("Usage:")
    println("test")
    println("gen <ilość liczb> <wielkość liczby> <klucz publiczny> <klucz prywatny>")
    println("enc <klucz publiczny> <plik do zaszyfrowania> <plik wynikowy>")
    println("dec <klucz prywatny> <zaszyfrowany plik> <plik wynikowy>")
  }

  def main(args: Array[String]) {

    if (args.length != 0) {
      args.head match {

        case "test" =>
          Zadanie2.main(null)

        case "gen" if args.length == 5 =>


          val mapper = new ObjectMapper() with ScalaObjectMapper
          mapper.registerModule(DefaultScalaModule)

          val rsa = RSA.gen(Integer.parseInt(args(2)), Integer.parseInt(args(1)))
          val publicKeyLocation = new File(args(3))
          val privateKeyLocation = new File(args(4))

          if (publicKeyLocation.exists()) {
            publicKeyLocation.delete()
          }

          if (privateKeyLocation.exists()) {
            privateKeyLocation.delete()
          }

          val publicKeyJson = mapper.writeValueAsString(rsa.pubKey)
          val privateKeyJson = mapper.writeValueAsString(rsa.privkey)

          FileUtils.writeStringToFile(publicKeyLocation, publicKeyJson)
          FileUtils.writeStringToFile(privateKeyLocation, privateKeyJson)

        case "enc" if args.length == 4 =>

          val publicKeyLocation = new File(args(1))
          val fileToEncrypt = new File(args(2))
          val resultFile = new File(args(3))

          if (resultFile.exists()) {
            resultFile.delete()
            resultFile.createNewFile()
          }

          val mapper = new ObjectMapper() with ScalaObjectMapper
          mapper.registerModule(DefaultScalaModule)

          val publicKey = mapper.readValue[PubKey](FileUtils.readFileToString(publicKeyLocation))

          val rsa = new RSA(null, publicKey)

          val fileAsByteArray = FileUtils.readFileToByteArray(fileToEncrypt)

          //  FileUtils.writeStringToFile(resultFile, s"${publicKey.n.bitLength}|")

          var k = (publicKey.n.bitLength / 8) + 1

          if (publicKey.n.bitLength % 8 == 0) {
            k = k - 1
          }

          k = k - 11


          fileAsByteArray.grouped(k)
           // .map(BigInt.apply)
            .map(rsa.RSAES_PKCS1_V1_5_ENCRYPT)
            .toList
            //.map(_.toByteArray)
            .foreach(FileUtils.writeByteArrayToFile(resultFile, _, true))

        /*
                  BigEndianConversions
                    .toIntArray(fileAsByteArray)
                    .map(BigInt.apply)
                    .map(rsa.enc)
                    .grouped(512 / 8)
                    .toList
        */
        //.map(e => e.for)

        // .map(_.toString() + "|")
        // .foreach(FileUtils.writeStringToFile(resultFile, _, true))

        case "dec" if args.length == 4 =>

          val privateKeyLocation = new File(args(1))
          val fileToDecrypt = new File(args(2))
          val resultFile = new File(args(3))

          if (resultFile.exists()) {
            resultFile.delete()
            resultFile.createNewFile()
          }


          val mapper = new ObjectMapper() with ScalaObjectMapper
          mapper.registerModule(DefaultScalaModule)

          val privateKey = mapper.readValue[PrivKey](FileUtils.readFileToString(privateKeyLocation))

          val rsa = new RSA(privateKey, null)

          val k = (privateKey.n.bitLength / 8) + 1

          val fileAsArray = FileUtils.readFileToByteArray(fileToDecrypt)

          fileAsArray.grouped(k)
            //.map(BigInt.apply)
            //.map(rsa.decCRT)
            .map(rsa.RSAES_PKCS1_V1_5_DECRYPT)
            .toList
            .foreach(FileUtils.writeByteArrayToFile(resultFile, _, true))


        // val fileAsString = FileUtils.readFileToString(fileToDecrypt)


        // val intArray = StringUtils.split(fileAsString, "|").map(BigInt.apply).map(rsa.decCRT).map(_.toInt)

        // val byteArray = BigEndianConversions.toByteArray(intArray)

        //  FileUtils.writeByteArrayToFile(resultFile, byteArray, true)


        case default =>
          printHelp()

      }
    } else {
      printHelp()
    }
  }
}
