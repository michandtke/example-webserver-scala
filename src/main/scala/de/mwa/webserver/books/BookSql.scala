package de.mwa.webserver.books

import slick.jdbc.H2Profile.api._
import slick.sql.SqlStreamingAction

import scala.concurrent.ExecutionContext

object BookSql extends BookTransfer {

  def createBooks: DBIO[Int] =
    sqlu"""create table books(
      id int not null,
      title varchar not null,
      subtitle varchar not null,
      desc varchar not null,
      done bool not null)"""

  def getAllBooks: SqlStreamingAction[Vector[Book], Book, Effect] = {
    sql"select * from books".as[Book]
  }

  def insertBook(b: Book): DBIO[Int] =
    sqlu"insert into books values (${b.id}, ${b.title}, ${b.subtitle}, ${b.desc}, ${b.done})"

  def getBook(id: Int)(implicit ec: ExecutionContext): DBIOAction[Option[Book], NoStream, Effect] =
    sql"select * from books where id = $id".as[Book].map(_.headOption)
}
