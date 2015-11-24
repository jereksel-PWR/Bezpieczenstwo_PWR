package pl.andrzejressel.bezpieczenstwo.lista4.zadanie2

import akka.actor.{Props, Actor}
import pl.andrzejressel.bezpieczenstwo.lista4.MathUtils

object Worker {
  def props = Props(classOf[Worker])
}

class Worker extends Actor {
  override def receive: Receive = {

    case StartWork(number) =>
      sender() ! EndWork(number, MathUtils.isPrime(number))

    case _ =>

  }
}
