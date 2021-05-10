import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object ToDoProtocol extends SprayJsonSupport with  DefaultJsonProtocol {
  implicit val todoFormat: RootJsonFormat[ToDo] = jsonFormat4(ToDo)
}