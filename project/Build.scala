import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName = "pokerbase"
  val appVersion = "2.0-SNAPSHOT"

  val ScalaVersion = "2.11.11"

  val appDependencies = Seq(
    "org.slf4s" %% "slf4s-api" % "1.7.12",
    "ch.qos.logback" % "logback-classic" % "1.1.2",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )


  lazy val root = Project(id = "pokerbase", base = file("."), settings = Seq(
    scalaVersion in ThisBuild := ScalaVersion
  ))
    .aggregate(model, jvmClient, parsers, rest)

  lazy val model = Project(id = "pokerbase-model", base = file("model"))
    .settings(libraryDependencies ++= appDependencies ++ Seq(
      "io.spray" %% "spray-json" % "1.3.3"
    ))

  lazy val jvmClient = Project(id = "pokerbase-jvm-client", base = file("jvm-client"))
    .dependsOn(model)
    .settings(libraryDependencies ++= appDependencies ++ Seq(
      "org.postgresql" % "postgresql" % "9.4.1208",
      "io.getquill" %% "quill-jdbc" % "1.2.1"
    ))

  lazy val parsers = Project(id = "pokerbase-parsers", base = file("parsers"))
    .settings(libraryDependencies ++= appDependencies ++ Seq(
      "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5"
    ))
    .dependsOn(model)

  lazy val rest = Project(id = "pokerbase-rest", base = file("rest"))
    .settings(libraryDependencies ++= appDependencies ++ Seq(
      "io.getquill" %% "quill-cassandra" % "1.2.1",
      "com.typesafe.akka" %% "akka-http" % "10.0.9",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.9",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.0.9" % Test
    ))
    .dependsOn(model, parsers)


}
