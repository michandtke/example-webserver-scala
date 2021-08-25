package de.mwa.webserver.books

import slick.jdbc.GetResult

trait BookTransfer {
  implicit val getBookResult: AnyRef with GetResult[Book] = GetResult(r => Book(r.<<, r.<<, r.<<, r.<<, r.<<))
}
