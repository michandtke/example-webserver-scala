package de.mwa.webserver

case class ToDo(id: Int, name: String, description: String, done: Boolean) extends Entity
