import scala.concurrent.Future

trait Storage {
  def put(stuff: Entity): Future[Unit]

  def get(id: Int): Future[Option[Entity]]
}
