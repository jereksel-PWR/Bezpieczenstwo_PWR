package pl.andrzejressel.bezpieczenstwo.lista4.zadanie2


import akka.actor._
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import pl.andrzejressel.bezpieczenstwo.lista4.MathUtils

import scala.collection.mutable

object Boss {
  def props(threadNumber: Int, numberSize: Int, numbers: Int) = Props(classOf[Boss], threadNumber, numberSize, numbers)

  private def makeRouter(ctx: ActorContext, threadNumber: Int): Router = {
    val routees = Vector.fill(threadNumber) {
      val r = ctx.actorOf(Worker.props)

      ActorRefRoutee(r)
    }

    Router(RoundRobinRoutingLogic(), routees)
  }

}

class Boss(threadNumber: Int, numberSize: Int, numbers: Int) extends Actor with akka.actor.ActorLogging {

  val router = Boss.makeRouter(context, threadNumber)

  def randomNumber = MathUtils.randomNumber(numberSize)

  var randomNumberHistory = mutable.MutableList[BigInt]()
  var i = 20
  var max = numbers
  var primeNumbers = mutable.MutableList[BigInt]()
  var resultTarget: ActorRef = _
  var sent = false

  override def receive: Receive = {

    case RequestResult =>
      resultTarget = sender()

    case StartWorkBoss =>
      (1 until i).foreach(e => {
        router.route(StartWork(getRandomNumber), self)
      })

    case EndWork(number, isPrime) =>
      if (isPrime && !primeNumbers.contains(number)) {
        primeNumbers += number
      }

      if (primeNumbers.size >= max && !sent) {
        resultTarget ! primeNumbers.toList
        sent = true
      } else {
        sendNumber(sender())
      }

    case _ =>
  }

  private def getRandomNumber: BigInt = {

    var bigInt = Stream.continually(randomNumber).dropWhile(randomNumberHistory.contains).head

    randomNumberHistory += bigInt

    bigInt
  }

  private def sendNumber(actor: ActorRef): Unit = {
    router.route(StartWork(getRandomNumber), self)
  }

}
