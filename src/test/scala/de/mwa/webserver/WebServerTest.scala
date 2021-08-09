package de.mwa.webserver

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.Future

class WebServerTest extends AnyWordSpec with Matchers with ScalatestRouteTest {
  "The get route should call the get function" in {
    var callCounter = 0
    var callId = -1

    Get("/todo/100") ~> WebServer.routes(id => {
      callCounter = callCounter + 1
      callId = id
      Future.successful(Some(ToDo(id, "n", "desc", done = false)))
    }, _ => Future.successful(""),
      _ => Future.successful(Seq.empty)) ~> check {
      callCounter should be(1)
      callId should be(100)
    }
  }

  "The get route should return the function result" in {
    val result = ToDo(100, "n", "desc", done = false)

    Get("/todo/100") ~> WebServer.routes(_ => Future.successful(Some(result)),
      _ => Future.successful(""),
      _ => Future.successful(Seq.empty)) ~> check {
      import de.mwa.webserver.ToDoProtocol._

      responseAs[ToDo] should be(result)
    }
  }

  "A failed function result should be propagated" in {
    val message = "This is an error. To bad!"

    Get("/todo/100") ~> WebServer.routes(
      _ => Future.failed(new IllegalArgumentException(message)),
      _ => Future.successful(""),
      _ => Future.successful(Seq.empty)) ~> check {
      responseAs[String] shouldEqual message
    }
  }
}
