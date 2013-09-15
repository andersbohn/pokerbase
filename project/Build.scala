import sbt._

object ApplicationBuild extends Build {

  val appName = "pokerbase"
  val appVersion = "1.0-SNAPSHOT"


//  val libraryDependencies = Seq("io.spray" %%  "spray-json" % "1.2.5")

  val appDependencies = Seq(
    //	"org.mongodb" %% "casbah" % "2.6.2"
    "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
    "org.reactivemongo" %% "play2-reactivemongo" % "0.9"

  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
  )

}
