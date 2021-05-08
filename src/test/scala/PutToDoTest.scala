import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class PutToDoTest extends AnyFlatSpec with should.Matchers {
  "A new ToDo" should "be inserted" in {
    // given
    val id = 0
    val todo = ToDo(id = id, name = "Name", description = "description", done = false)
    val storage = new TestStorage()

    // when
    Await.ready(Accessor.create(storage, todo), 1.second)
    val id0: ToDo = Await.result(storage.get(id), 1.second)

    // then
    id0 should be (todo)
  }
}
