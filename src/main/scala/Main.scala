import akka.actor.ActorSystem

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.io.StdIn

object Main extends App {
  implicit val system: ActorSystem = ActorSystem("my-system")
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  val storage = new TestStorage

  val todo = ToDo(0, "The first one", "This is the first todo for testing purposes. Does the setup work?", done = false)
  Await.ready(storage.put(todo), Duration.Inf)

  val server = WebServer.start(Accessor.get(storage, _))

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  WebServer.terminate(server)
    .onComplete(_ => system.terminate())
}
