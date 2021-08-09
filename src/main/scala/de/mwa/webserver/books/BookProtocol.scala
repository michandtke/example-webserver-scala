package de.mwa.webserver.books

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object BookProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val bookFormat: RootJsonFormat[Book] = jsonFormat5(Book)
}
