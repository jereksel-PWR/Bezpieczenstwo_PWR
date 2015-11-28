package pl.andrzejressel.bezpieczenstwo.lista4

import java.io.File

import com.google.common.io.Files
import org.bouncycastle.pqc.math.linearalgebra.BigEndianConversions
import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.RSA

object Zadanie1Plik extends App {

  val plik = new File("D:\\unnamed2.patch")
  val temp = Files.createTempDir
  val tempFile = new File(temp + File.separator + "temp.patch")
  val tempFile2 = new File(temp + File.separator + "temp2.patch")

  tempFile.createNewFile()
  tempFile2.createNewFile()

  println("Temp dir: " + temp)

  val a = Files.asByteSource(plik).read()

  val rsa = RSA.gen(512, 2)

  println("Szyfrowanie")
  val encrypted = BigEndianConversions.toIntArray(a).map(BigInt.apply).map(rsa.enc)
  println("Deszyfrowanie")
  val decrypted = BigEndianConversions.toByteArray(encrypted.map(rsa.decCRT).map(_.toInt))

  Files.write(a, tempFile)

  Files.write(decrypted, tempFile2)

}
