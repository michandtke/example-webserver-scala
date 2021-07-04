
import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.StrictLogging

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

object WebServer extends Directives with StrictLogging {

  //  def start(fGet: Function[Int, Future[ToDo]],
  //            fPut: Function[ToDo, Future[Unit]])(implicit system: ActorSystem): Future[Http.ServerBinding] = {
  def start(fGet: Function[Int, Future[Option[Entity]]], fCreate: Function[Entity, Future[String]])(implicit system: ActorSystem): Future[Http.ServerBinding] = {
    val route = routes(fGet, fCreate)

    val host = "0.0.0.0"
    val port: Int = sys.env.getOrElse("PORT", "8080").toInt
    logger.error(s"My port: $port")
    println(s"My port: $port")

    val binding = Http().newServerAt(host, port).bind(route)

    binding.andThen(_ => println(s"Server online at $host:$port\nPress RETURN to stop..."))(system.dispatcher)
  }

  def routes(fGet: Function[Int, Future[Option[Entity]]], fCreate: Function[Entity, Future[String]]): Route = {
    val todos =
      pathPrefix("todo" / IntNumber) { id =>
        get {
          val result: Future[Option[Entity]] = fGet(id)
          import ToDoProtocol._
          onComplete(result) {
            case Success(res: Option[ToDo]) => complete(res)
            case Failure(err) => complete(err.getMessage)
          }
        }
      }

    import BookProtocol._

    val books =
      pathPrefix("book" / IntNumber) { id =>
        get {
          val result: Future[Option[Entity]] = fGet(id)
          import BookProtocol._
          onComplete(result) {
            case Success(res: Option[Book]) => complete(res)
            case Failure(err) => complete(err.getMessage)
          }
        }
      } ~
        path("book" / "add") {
          put {
            entity(as[Book]) { book =>
              onSuccess(fCreate(book)) { result =>
                complete((StatusCodes.Created, result))
              }
            }
          }
        }

    import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
    cors() {todos ~ books}
  }

  def terminate(bindingFuture: Future[Http.ServerBinding])(implicit context: ExecutionContext): Future[Done] = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
  }
}
