package pl.andrzejressel.bezpieczenstwo.lista4.benchmark

import java.security.SecureRandom

import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.RSA

object Benchmark extends App {

  val secRand = new SecureRandom()

  //3 klucze po 1024 bity ~ 3072 bity klucza końcowego
  val rsa = RSA.gen(1024, 3)

  //128 bitów

  //Generowanie wiadomości
  val message = Array.fill[Byte](10* 1000000)(0x00)
 // val message = Array.fill[Byte](16)(0x00)

  secRand.nextBytes(message)


  for (
    j <- List(1, 10, 100, 1000, 10000)
  //  i <- 1 to 8
  ) {

    println(s"$j wiadomości")

    val t0 = System.nanoTime()

    for (i <- 1 to j) {
      val enc = rsa.enc(message)
      val dec = rsa.dec(enc)
    }

    val t1 = System.nanoTime()

    println(s"Czas: ${(t1 - t0) / 1000000000.0}s")


    println()

  }


}
