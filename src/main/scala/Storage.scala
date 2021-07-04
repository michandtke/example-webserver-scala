import scala.concurrent.Future
import Types._

trait Storage {
  def put(stuff: Entity): Future[Unit]

  def get(id: Int): Future[Option[Entity]]

  def all(types: Types): Future[Seq[Entity]]
}
