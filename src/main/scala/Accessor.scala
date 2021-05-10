import scala.concurrent.Future

object Accessor {
  def create(storage: Storage, value: ToDo): Future[Unit] = storage.put(value)

  def get(storage: Storage, id: Int): Future[ToDo] = storage.get(id)
}
