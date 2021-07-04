name := "examples"

version := "0.1"

scalaVersion := "2.13.5"

enablePlugins(JavaAppPackaging)

libraryDependencies += "com.typesafe.akka" %% "akka-http" % "10.2.4"
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.14"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.14"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.7" % "test"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3"
libraryDependencies += "ch.megard" %% "akka-http-cors" % "1.1.1"

val AkkaVersion = "2.6.14"
val AkkaHttpVersion = "10.2.4"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion,
)
