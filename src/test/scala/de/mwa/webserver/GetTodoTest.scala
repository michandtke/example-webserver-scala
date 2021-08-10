package de.mwa.webserver

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

class GetTodoTest extends AnyFlatSpec with should.Matchers {
  "An unknown todo" should "return None" in {
    // given
    val id = 100
    val storage = new TestStorage()

    // when
    val id0: Option[Entity] = Await.result(Accessor.get(storage, id), 1.second)

    // then
    id0 should be (None)
  }
}
