import sbt._
import Keys._

object ApplicationBuild extends Build {

  val appName = "pokerbase"
  val appVersion = "2.0-SNAPSHOT"

  val ScalaVersion = "2.11.11"

  val appDependencies = Seq(
    "io.spray" %% "spray-json" % "1.3.3",
    //     "org.scalactic" %% "scalactic" % "3.0.1",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )

  lazy val root = Project(id = "pokerbase", base = file("."), settings = Seq(
    scalaVersion := ScalaVersion
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
