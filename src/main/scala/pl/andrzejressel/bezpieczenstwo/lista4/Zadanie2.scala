package pl.andrzejressel.bezpieczenstwo.lista4

import pl.andrzejressel.bezpieczenstwo.lista4.zadanie2.PR

object Zadanie2 extends App {

  for (
    j <- List(256, 512, 1024, 2048, 3072, 7680);
    i <- 1 to 8
  ) {

    println(s"Ilość liczb: $i Wielkość liczby: $j")

    val t0 = System.nanoTime()

    val numbers = PR.getPrimes(Runtime.getRuntime.availableProcessors(), j, i)

    val t1 = System.nanoTime()

    println(s"Czas: ${(t1 - t0) / 1000000000.0}s")

    println()

  }

}
