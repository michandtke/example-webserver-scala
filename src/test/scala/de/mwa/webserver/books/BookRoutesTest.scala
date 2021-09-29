package de.mwa.webserver.books

import akka.http.scaladsl.server.RouteResult
import akka.http.scaladsl.testkit.ScalatestRouteTest
import de.mwa.webserver.WebServer
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Future

class BookRoutesTest extends AnyWordSpec with Matchers with ScalatestRouteTest {
  private val failedFuture = Future.failed(new IllegalArgumentException())

  "The get route should call the get function" in {
    var callCounter = 0
    var callId = -1

    val getResult = (id: Int) => if (id == 100) {
      callCounter = callCounter + 1
      callId = id
      Future.successful(None)
    } else failedFuture

    val routes = new BookRoutes(getResult, _ => failedFuture, _ => failedFuture).books()

    Get("/book/100") ~> routes ~> check {
      callCounter should be(1)
      callId should be(100)
    }
  }

  "The get route should return the function result" in {
    val result = Book(100, "title", "subtitle", "desc", done = false)

    val getResult = (id: Int) => if (id == 100) { Future.successful(Some(result)) } else failedFuture

    val routes = new BookRoutes(getResult, _ => failedFuture, _ => failedFuture).books()

    import BookProtocol._
    Get("/book/100") ~> routes ~> check {
      responseAs[Book] should be(result)
    }
  }

  "A failed function result should be propagated" in {
    val message = "This is an error. To bad!"

    val routes = new BookRoutes(_ => Future.failed(new IllegalArgumentException(message)), _ => failedFuture, _ => failedFuture).books()

    Get("/book/100") ~> routes ~> check {
      responseAs[String] shouldEqual message
    }
  }
}
