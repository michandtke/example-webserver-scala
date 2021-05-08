import scala.concurrent.Future

trait Storage {
  def put(stuff: ToDo): Future[Unit]

  def get(id: Int): Future[ToDo]
}
