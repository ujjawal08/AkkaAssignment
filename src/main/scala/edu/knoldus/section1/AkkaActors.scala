package edu.knoldus.section1
import akka.actor._
import edu.knoldus.section1.Ping.CreateChild

case object PingMessage
case object PongMessage

object Ping {
  case class CreateChild(name: String)
}

class Ping extends Actor with ActorLogging {

  override def receive: Receive = {
    case CreateChild(name) =>
      println(s"${self.path} Creating child! $name")
      val childRef = context.actorOf(Props[Pong], name)
      childRef ! "Ping"
    case PongMessage => log.info("Ping")
  }
}
class Pong extends Actor with ActorLogging {
  def receive:Receive = {
    case PingMessage => log.info("Pong")
      sender() ! "Pong"
  }
}
object AkkaActors extends App {
  val system = ActorSystem("PingPongSystem")
  val ping = system.actorOf(Props[Ping], name = "Ping")
  ping ! CreateChild("Pong")


}