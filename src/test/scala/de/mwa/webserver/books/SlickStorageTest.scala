package de.mwa.webserver.books

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import slick.jdbc.JdbcBackend
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._

class SlickStorageTest extends AnyFlatSpec with should.Matchers {

  "Slick storage" should "return nothing if no entries" in {
    // given
    implicit val db: JdbcBackend.Database = Database.forConfig("h2mem1")
    Await.ready(SlickStorage.setup(), 1.second)

    // when
    val allBefore = Await.result(SlickStorage.all(), 1.second)

    // then
    allBefore should be (empty)
    db.close()
  }

  it should "return an entry if it was put in" in {
    // given
    implicit val db: Database = Database.forConfig("h2mem1")
    Await.ready(SlickStorage.setup(), 1.second)
    val book = Book(1, "title", "subtitle", "desc", done = true)

    // when
    Await.ready(SlickStorage.put(book), 1.second)
    val after = Await.result(SlickStorage.all(), 1.second)

    // then
    after should contain only book
    db.close()
  }

  it should "return the inserted entry if asked directly" in {
    // given
    implicit val db: Database = Database.forConfig("h2mem1")
    Await.ready(SlickStorage.setup(), 1.second)
    val book = Book(1, "title", "subtitle", "desc", done = true)

    // when
    Await.ready(SlickStorage.put(book), 1.second)
    val after = Await.result(SlickStorage.get(1), 1.second)

    // then
    after should be(defined)
    after should contain(book)
    db.close()
  }
}
