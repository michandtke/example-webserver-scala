import Types.Types

import scala.concurrent.{ExecutionContext, Future}

object Accessor {
  def all(storage: TestStorage, types: Types): Future[Seq[Entity]] = storage.all(types)

  def create(storage: Storage, value: Entity): Future[String] = storage.put(value).map(_ => "Created")(ExecutionContext.global)

  def get(storage: Storage, id: Int): Future[Option[Entity]] = {
    storage.get(id)
  }
}
