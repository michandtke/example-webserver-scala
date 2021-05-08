import scala.concurrent.Future

object Accessor {
  def create(storage: Storage, value: ToDo): Future[Unit] = {
    storage.put(value)
  }
}
