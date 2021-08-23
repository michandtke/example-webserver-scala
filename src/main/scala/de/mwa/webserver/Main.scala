package de.mwa.webserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.StrictLogging
import de.mwa.webserver.books.{BookRoutes, SlickStorage}
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

object Main extends App with StrictLogging {
  implicit val system: ActorSystem = ActorSystem("my-system")

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  logger.info("Starting uo the server ...")

  // implicit val db: JdbcBackend.Database = Database.forConfig("postgres")
  implicit val db: JdbcBackend.Database = Database.forConfig("h2mem1")

  val storage = new TestStorage
  val books = BookRoutes()

  val todo = ToDo(0, "The first one", "This is the first todo for testing purposes. Does the setup work?", done = false)
  Await.ready(storage.put(todo), Duration.Inf)

  val server: Future[Http.ServerBinding] =
    WebServer.start(Accessor.get(storage, _),
      Accessor.create(storage, _),
      Accessor.all(storage, _),
      books)

  //  server.andThen(_ => system.terminate())
}
