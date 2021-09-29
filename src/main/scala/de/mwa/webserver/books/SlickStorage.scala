package de.mwa.webserver.books

import slick.dbio.DBIO
import slick.jdbc.JdbcBackend

import scala.concurrent.{ExecutionContextExecutor, Future}

object SlickStorage {

  implicit val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  def setup()(implicit database: JdbcBackend.Database): Future[Unit] = exec(BookSql.createBooks, -1).map(_ => ())

  private def exec[T](action: DBIO[T], defaultValue: T)(implicit database: JdbcBackend.Database): Future[T] =
    database.run(action).recover {
      case t: Throwable =>
        println(t)
        defaultValue
    }

  def put(b: Book)(implicit database: JdbcBackend.Database): Future[String] = exec(BookSql.insertBook(b).map(_ => "Created"), "Error")

  def get(id: Int)(implicit database: JdbcBackend.Database): Future[Option[Book]] = exec(BookSql.getBook(id), None)

  def all()(implicit database: JdbcBackend.Database): Future[Seq[Book]] = exec(BookSql.getAllBooks, Vector.empty)
}
