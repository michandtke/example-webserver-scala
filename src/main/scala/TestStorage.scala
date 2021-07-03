import java.util.concurrent.ConcurrentHashMap

import scala.concurrent.Future

class TestStorage extends Storage {
  val map = new ConcurrentHashMap[Int, Entity]()

  override def put(stuff: Entity): Future[Unit] = {
    Future.successful(map.put(stuff.id, stuff))
  }

  override def get(id: Int): Future[Option[Entity]] = {
    Future.successful(Option(map.get(id)))
  }
}
