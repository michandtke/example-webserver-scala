package de.mwa.webserver.books

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import com.typesafe.scalalogging.StrictLogging
import slick.jdbc.JdbcBackend

import scala.concurrent.Future
import scala.util.{Failure, Success}

class BookRoutes(fGet: Function[Int, Future[Option[Book]]],
                 fCreate: Function[Book, Future[String]],
                 fAll: Function[Unit, Future[Seq[Book]]]) extends Directives with StrictLogging {


  def books(): Route = {

    import de.mwa.webserver.books.BookProtocol._

    path("books") {
      get {
        val result = fAll(())
        onComplete(result) {
          case Success(res: Seq[Book]) => complete(res)
          case Failure(err) => complete(err.getMessage)
        }
      }
    } ~
      path("book" / IntNumber) { id =>
        get {
          val result: Future[Option[Book]] = fGet(id)

          onComplete(result) {
            case Success(res: Option[Book]) => complete(res)
            case Failure(err) => complete(err.getMessage)
          }
        }
      } ~
      path("book" / "add") {
        put {
          logger.info("We have a put request!")
          entity(as[Book]) { book =>
            logger.info(s"We have a book! $book")
            onSuccess(fCreate(book)) { result =>
              complete((StatusCodes.Created, result))
            }
          }
        }
      }
  }
}

object BookRoutes {
  def apply()(implicit database: JdbcBackend.Database): Route = new BookRoutes(SlickStorage.get, SlickStorage.put, _ => SlickStorage.all).books()
}
