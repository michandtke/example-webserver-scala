package de.mwa.webserver

import de.mwa.webserver.Types.Types

import scala.concurrent.Future

trait Storage {
  def setup(): Future[Unit]

  def put(stuff: Entity): Future[Unit]

  def get(id: Int): Future[Option[Entity]]

  def all(types: Types): Future[Seq[Entity]]
}
