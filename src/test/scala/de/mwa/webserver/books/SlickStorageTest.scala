package de.mwa.webserver.books

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should
import slick.jdbc.JdbcBackend.Database

import scala.concurrent.Await
import scala.concurrent.duration._

class SlickStorageTest extends AnyFlatSpec with should.Matchers {

  "Slick storage" should "return nothing if no entries" in {
    // given
    val db = Database.forConfig("h2mem1")
    val slickStorage = new SlickStorage(db)
    Await.ready(slickStorage.setup(), 1.second)

    // when
    val allBefore = Await.result(slickStorage.all(), 1.second)

    // then
    allBefore should be (empty)
  }

  it should "return an entry if it was put in" in {
    // given
    val db = Database.forConfig("h2mem1")
    val slickStorage = new SlickStorage(db)
    Await.ready(slickStorage.setup(), 1.second)
    val book = Book(1, "title", "subtitle", "desc", done = true)

    // when
    Await.ready(slickStorage.put(book), 1.second)
    val after = Await.result(slickStorage.all(), 1.second)

    // then
    after should contain only book
  }

  it should "return the inserted entry if asked directly" in {
    // given
    val db = Database.forConfig("h2mem1")
    val slickStorage = new SlickStorage(db)
    Await.ready(slickStorage.setup(), 1.second)
    val book = Book(1, "title", "subtitle", "desc", done = true)

    // when
    Await.ready(slickStorage.put(book), 1.second)
    val after = Await.result(slickStorage.get(1), 1.second)

    // then
    after should be(defined)
    after should contain(book)
  }
}
