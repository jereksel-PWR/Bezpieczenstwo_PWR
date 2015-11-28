package pl.andrzejressel.bezpieczenstwo.lista4

import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.RSA

object Zadanie1Nowe extends App {

  val rsa = RSA.gen(512, 20)

  val wiadomosc = BigInt(123123123)

  val cipherText = rsa.enc(wiadomosc)

  println("Deszyfrowane Standardowe")

  val dec2 = rsa.decStandard(cipherText)

  println("Deszyfrowane Szybkie")

  val dec1 = rsa.decCRT(cipherText)

  val i = 0

}
