package pl.andrzejressel.bezpieczenstwo.lista4.zadanie2

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}

import scala.concurrent.Await

trait Message

case class StartWorkBoss() extends Message

case class StartWork(number: BigInt) extends Message

case class EndWork(number: BigInt, status: Boolean) extends Message

case object RequestResult extends Message

object PR {

  def gimmePrimes(threadNumber: Int, numberSize: Int, numbers: Int): List[BigInt] = {

    val config = ConfigFactory.load()
      .withValue("akka.log-dead-letters", ConfigValueFactory.fromAnyRef("0"))

    val system = ActorSystem(s"PrimeNumbers_${System.nanoTime()}", config)
    val boss = system.actorOf(Boss.props(threadNumber, numberSize, numbers))

    implicit val timeout = Timeout(200, TimeUnit.MINUTES)

    boss ! StartWorkBoss
    val result = Await.result(boss ? RequestResult, timeout.duration).asInstanceOf[List[BigInt]]

    system.terminate()

    return result
  }
}
