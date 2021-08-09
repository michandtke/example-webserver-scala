package de.mwa.webserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import de.mwa.webserver.books.SlickStorage
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("my-system")

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  implicit val db: JdbcBackend.Database = Database.forConfig("postgres")

  val storage = new TestStorage

  val todo = ToDo(0, "The first one", "This is the first todo for testing purposes. Does the setup work?", done = false)
  Await.ready(storage.put(todo), Duration.Inf)

  val server: Future[Http.ServerBinding] =
    WebServer.start(Accessor.get(storage, _),
      Accessor.create(storage, _),
      Accessor.all(storage, _))

  //  server.andThen(_ => system.terminate())
}