import sbt._

object ApplicationBuild extends Build {

  val appName = "pokerbase"
  val appVersion = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    //	"org.mongodb" %% "casbah" % "2.6.2"
    "org.reactivemongo" %% "play2-reactivemongo" % "0.9"
  )


  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here      
  )

}
