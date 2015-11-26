package pl.andrzejressel.bezpieczenstwo.lista4

import pl.andrzejressel.bezpieczenstwo.lista4.zadanie1.RSA

object Zadanie2Nowe extends App {

  val (priv, pub) = RSA.gen(256, 20)

  val wiadomosc = BigInt(123123123)

  val cipherText = RSA.enc(wiadomosc, pub)

  val dec1 = RSA.decCRT(cipherText, priv)
  val dec2 = RSA.decStandard(cipherText, priv)

  val i = 0

}
