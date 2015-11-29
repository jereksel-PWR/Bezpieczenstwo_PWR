import java.io.File

import com.google.common.io.Files
import org.apache.commons.io.FileUtils
import org.scalatest.{Matchers, FlatSpec}
import pl.andrzejressel.bezpieczenstwo.lista4.Main

import scala.util.Random

class FileEncryptionDecryption extends FlatSpec with Matchers {

  "generating keys and encryption/decryption of files" must "work" in {

    for (i <- 0 to 10) {

      //println(i)

      val random = new Random()

      val tempDir = Files.createTempDir()

      val privKey = new File(tempDir + File.separator + "priv.key")
      val pubKey = new File(tempDir + File.separator + "pub.key")

      val baseFile = new File(tempDir + File.separator + "base.file")
      val encFile = new File(tempDir + File.separator + "enc.file")
      val decFile = new File(tempDir + File.separator + "dec.file")


      Main.main(Array("gen", "8", "256", pubKey.getAbsolutePath, privKey.getAbsolutePath))

      val fileSizeInBytes = 100000

      val byteArray = Array.fill[Byte](fileSizeInBytes)(0)

      random.nextBytes(byteArray)

      FileUtils.writeByteArrayToFile(baseFile, byteArray)

      Main.main(Array("enc", pubKey.getAbsolutePath, baseFile.getAbsolutePath, encFile.getAbsolutePath))

      Main.main(Array("dec", privKey.getAbsolutePath, encFile.getAbsolutePath, decFile.getAbsolutePath))

      FileUtils.readFileToByteArray(baseFile) shouldBe  FileUtils.readFileToByteArray(decFile)
      FileUtils.readFileToByteArray(baseFile) should not equal FileUtils.readFileToByteArray(encFile)
      FileUtils.readFileToByteArray(decFile) should not equal FileUtils.readFileToByteArray(encFile)


      FileUtils.deleteDirectory(tempDir)

    //  println()

    }


  }


}
