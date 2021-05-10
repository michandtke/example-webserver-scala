import java.util.concurrent.ConcurrentHashMap

import scala.concurrent.Future

class TestStorage extends Storage {
  val map = new ConcurrentHashMap[Int, ToDo]()

  override def put(stuff: ToDo): Future[Unit] = {
    Future.successful(map.put(stuff.id, stuff))
  }

  override def get(id: Int): Future[ToDo] = {
    Future.successful(map.get(id))
  }
}
