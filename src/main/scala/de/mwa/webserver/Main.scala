package de.mwa.webserver

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.scalalogging.StrictLogging
import de.mwa.webserver.books.{BookRoutes, SlickStorage}
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.duration.{Duration, DurationInt}
import scala.concurrent.{Await, ExecutionContextExecutor, Future}

object Main extends App with StrictLogging {
  implicit val system: ActorSystem = ActorSystem("my-system")

  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val user = system.settings.config.getString("postgres.properties.user")
  val pwd = system.settings.config.getString("postgres.properties.password")

  logger.info(s"Starting uo the server for $user ($pwd)...")
  println(s"Starting uo the server for $user ($pwd)...")

  implicit val db: JdbcBackend.Database = Database.forConfig("postgres")
  // implicit val db: JdbcBackend.Database = Database.forConfig("h2mem1")

  val slickSetup = SlickStorage.setup()

  val storage = new TestStorage
  val books = BookRoutes()

  //val todo = ToDo(0, "The first one", "This is the first todo for testing purposes. Does the setup work?", done = false)
  //Await.ready(storage.put(todo), Duration.Inf)

  val server: Future[Http.ServerBinding] = slickSetup.flatMap(_ => WebServer.start(Accessor.get(storage, _),
    Accessor.create(storage, _),
    Accessor.all(storage, _),
    books))





  //  server.andThen(_ => system.terminate())
}
