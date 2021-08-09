package de.mwa.webserver

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import ch.megard.akka.http.cors.scaladsl.model.HttpOriginMatcher
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.typesafe.scalalogging.StrictLogging
import de.mwa.webserver.Types.Types
import de.mwa.webserver.books.BookRoutes
import slick.jdbc.JdbcBackend

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object WebServer extends Directives with StrictLogging {

  def start(fGet: Function[Int, Future[Option[Entity]]],
            fCreate: Function[Entity, Future[String]],
            fAll: Function[Types, Future[Seq[Entity]]])(implicit system: ActorSystem, db: JdbcBackend.Database): Future[Http.ServerBinding] = {
    val route = routes(fGet, fCreate, fAll)

    val host = "0.0.0.0"
    val port: Int = sys.env.getOrElse("PORT", "8080").toInt
    logger.error(s"My port: $port")
    println(s"My port: $port")

    val binding = Http().newServerAt(host, port).bind(route)

    binding.andThen(_ => println(s"Server online at $host:$port\nPress RETURN to stop..."))(system.dispatcher)
  }

  def routes(fGet: Function[Int, Future[Option[Entity]]],
             fCreate: Function[Entity, Future[String]],
             fAll: Function[Types, Future[Seq[Entity]]])(implicit db: JdbcBackend.Database): Route = {
    import ToDoProtocol._
    val todos = {
      path("todos") {
        get {
          val result = fAll(Types.Todo)
          onComplete(result) {
            case Success(res: Seq[ToDo]) => complete(res)
            case Failure(err) => complete(err.getMessage)
          }
        }
      } ~
        path("todo" / IntNumber) { id =>
          get {
            val result: Future[Option[Entity]] = fGet(id)
            onComplete(result) {
              case Success(res: Option[ToDo]) => complete(res)
              case Failure(err) => complete(err.getMessage)
            }
          }
        } ~
        path("todo" / "add") {
          put {
            entity(as[ToDo]) { todo =>
              onSuccess(fCreate(todo)) { result =>
                complete((StatusCodes.Created, result))
              }
            }
          }
        }
    }

    import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
    cors(settings) {
      todos ~ BookRoutes()
    }
  }

  val settings: CorsSettings = CorsSettings
    .defaultSettings
    .withAllowedMethods(Seq(GET, PUT, POST, HEAD, OPTIONS, DELETE))
    .withAllowedOrigins(HttpOriginMatcher.*)

  def terminate(bindingFuture: Future[Http.ServerBinding])(implicit context: ExecutionContext): Future[Done] = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
  }
}
