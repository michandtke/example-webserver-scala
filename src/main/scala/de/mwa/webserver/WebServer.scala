package de.mwa.webserver

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContext, Future}

object WebServer extends Directives with StrictLogging {

  def start(books: Route)(implicit system: ActorSystem): Future[Http.ServerBinding] = {

    val host = "0.0.0.0"
    val port: Int = sys.env.getOrElse("PORT", "8080").toInt
    logger.info(s"My port: $port")
    println(s"My port: $port")

    val binding = Http().newServerAt(host, port).bind(books)

    binding.andThen(_ => println(s"Server online at $host:$port\nPress RETURN to stop..."))(system.dispatcher)
  }

  def terminate(bindingFuture: Future[Http.ServerBinding])(implicit context: ExecutionContext): Future[Done] = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
  }
}
