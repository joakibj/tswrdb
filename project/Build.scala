import sbt._

object TswRdb extends Build {
  lazy val tswrdb = Project("tswrdb", file(".")) aggregate(api, cmdui)
  lazy val api = Project("tswrdb-api", file("tswrdb-api"))
  lazy val cmdui = Project("tswrdb-cmdui", file("tswrdb-cmdui")) dependsOn(api)
}