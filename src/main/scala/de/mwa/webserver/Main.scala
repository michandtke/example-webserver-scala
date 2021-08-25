package de.mwa.webserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.StrictLogging
import de.mwa.webserver.books.{BookRoutes, SlickStorage}
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.{ExecutionContextExecutor, Future}

object Main extends App with StrictLogging {
  implicit val system: ActorSystem = ActorSystem("my-system")

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  logger.info(s"Starting uo the server ...")

  implicit val db: JdbcBackend.Database = Database.forConfig("postgres")

  val slickSetup = SlickStorage.setup()

  val books = BookRoutes()

  val server: Future[Http.ServerBinding] = slickSetup.flatMap(_ => WebServer.start(books))

  //  server.andThen(_ => system.terminate())
}
