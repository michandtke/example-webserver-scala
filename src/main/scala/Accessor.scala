import scala.concurrent.{ExecutionContext, Future}

object Accessor {
  def create(storage: Storage, value: ToDo): Future[Unit] = storage.put(value)

  def get(storage: Storage, id: Int): Future[Option[ToDo]] = {
    storage.get(id).map {
      case null => None
      case res => Some(res)
    }(ExecutionContext.global)
  }
}
