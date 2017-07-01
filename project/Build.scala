import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName = "pokerbase"
  val appVersion = "2.0-SNAPSHOT"

  val ScalaVersion = "2.11.11"

  val appDependencies = Seq(
    "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.5",
    "io.spray" %% "spray-json" % "1.3.3",
    //     "org.scalactic" %% "scalactic" % "3.0.1",
    // https://mvnrepository.com/artifact/postgresql/postgresql
//    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "org.postgresql" % "postgresql" % "9.4.1208",
    "io.getquill" %% "quill-jdbc" % "1.2.1",

    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )

  lazy val root = Project(id = "pokerbase", base = file("."), settings = Seq(
    scalaVersion in ThisBuild := ScalaVersion
  ))
    .aggregate(model, jvmClient, parsers)

  lazy val model = Project(id = "pokerbase-model", base = file("model"))
    .settings(libraryDependencies ++= appDependencies)

  lazy val jvmClient = Project(id = "pokerbase-jvm-client", base = file("jvm-client"))
    .dependsOn(model)
    .settings(libraryDependencies ++= appDependencies)

  lazy val parsers = Project(id = "pokerbase-parsers", base = file("parsers"))
    .settings(libraryDependencies ++= appDependencies)
    .dependsOn(model)


}
