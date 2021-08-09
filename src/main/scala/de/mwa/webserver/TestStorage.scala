package de.mwa.webserver

import de.mwa.webserver.books.Book
import de.mwa.webserver.Types.Types

import java.util.concurrent.ConcurrentHashMap
import scala.concurrent.Future
import scala.jdk.CollectionConverters.CollectionHasAsScala


class TestStorage extends Storage {

  override def setup(): Future[Unit] = Future.successful(())

  val map = new ConcurrentHashMap[Int, Entity]()

  override def put(stuff: Entity): Future[Unit] = {
    Future.successful(map.put(stuff.id, stuff))
  }

  override def get(id: Int): Future[Option[Entity]] = {
    Future.successful(Option(map.get(id)))
  }

  override def all(types: Types): Future[Seq[Entity]] = {
    types match {
      case Types.Book => Future.successful(map.values().asScala.filter(ent => ent.isInstanceOf[Book]).toSeq)
      case Types.Todo => Future.successful(map.values().asScala.filter(ent => ent.isInstanceOf[ToDo]).toSeq)
    }
  }
}
