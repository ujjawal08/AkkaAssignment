package edu.knoldus.section2

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

object Ping{
  case object PingWork
  case object Pong
  case class childPong(name: String)
  case class End(receivedPings: Int)
  case object Messages
  case class ThrowException()
  case class GetPongSum(sum: Option[Int])
}
class Ping extends Actor with ActorLogging {


  import Ping._

  var sum: Int = 0

  override def receive: Receive = {

    case childPong(name) =>

      val childRef = context.actorOf(Props[Pong], name)
      for {
        _ <- 1 to 10000
      } yield childRef ! PingWork

      childRef ! GetPongSum(Some(sum))
      childRef ! ThrowException
      childRef ! GetPongSum(None)

    case Pong => sum += 1
      println("pong")
    case End(sum) => {
      println(s"The sum is $sum")
    }
    case GetPongSum(s) => println(s)

  }
}

class Pong extends Actor with ActorLogging {

  import Ping._

  var sum: Int = 0

  def doWork(): Int = {
    Thread sleep 1000
    1
  }

  override def receive: Receive = {
    case PingWork => sum += 1
      val future: Future[Int] = Future {
        sum += doWork
        sum
      }
      Await.result(future, Duration.Inf)
      if (sum < 10000)
        sender ! Pong
      else if (sum == 10000) {
        sender ! End(sum)

        sender ! GetPongSum(Some(sum))
      }
      println("ping")

    case End(count) => println(s"Count is : $count")

    case ThrowException => println(throw new Exception())
  }
}

object StateOfActors extends App {
  import Ping._
  val system = ActorSystem("PingPongSystem")
  val ping = system.actorOf(Props[Ping], "ping")
  ping ! childPong("Child")

}
