import java.util.concurrent.ConcurrentHashMap

import Types.Types

import scala.concurrent.Future
import scala.jdk.CollectionConverters.CollectionHasAsScala


class TestStorage extends Storage {
  val map = new ConcurrentHashMap[Int, Entity]()

  override def put(stuff: Entity): Future[Unit] = {
    Future.successful(map.put(stuff.id, stuff))
  }

  override def get(id: Int): Future[Option[Entity]] = {
    Future.successful(Option(map.get(id)))
  }

  override def all(types: Types): Future[Seq[Entity]] = {
    Future.successful(map.values().asScala.filter(ent => ent.isInstanceOf).toSeq)
  }
}
