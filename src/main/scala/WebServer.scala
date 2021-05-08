import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives.{complete, get, path}

import scala.concurrent.{ExecutionContext, Future}

object WebServer {

  def start()(implicit system: ActorSystem): Future[Http.ServerBinding] = {

    val route = path("hello") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
      }
    }

    Http().newServerAt("localhost", 8080).bind(route)
  }

  def terminate(bindingFuture: Future[Http.ServerBinding])(implicit context: ExecutionContext): Future[Done] = {
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
  }
}
