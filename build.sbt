import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.cross.CrossType
import play.sbt.PlayImport._
import play.sbt.PlayScala
import playscalajs.ScalaJSPlay
import sbt.Project.projectToRef
import sbt._
import Keys._
import play.routes.compiler.InjectedRoutesGenerator
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

lazy val clients = Seq(client)
lazy val scalaV = "2.11.7"
lazy val library = "0.7-SNAPSHOT"

lazy val server = (project in file("server")).settings(
  scalaVersion := scalaV,
  scalaJSProjects := clients,
  pipelineStages := Seq(scalaJSProd, gzip),
  resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases",
  libraryDependencies ++= Seq(
    evolutions,
    "com.vmunier" %% "play-scalajs-scripts" % "0.3.0",
    "org.webjars" %% "webjars-play" % "2.4.0-1",
    "org.webjars" % "jquery" % "1.11.1",
    "com.lihaoyi" %% "upickle" % "0.3.4",
    "org.webjars" % "angularjs" % "1.4.8",
    "com.typesafe.play" %% "play-slick" % "1.0.1",
    "com.typesafe.play" %% "play-slick-evolutions" % "1.0.1",
    "com.github.tminglei" %% "slick-pg" % "0.9.1",
    "joda-time" % "joda-time" % "2.7",
    "org.joda" % "joda-convert" % "1.7",
    "net.codingwell" %% "scala-guice" % "4.0.0",
    "org.postgresql" % "postgresql" % "9.4-1205-jdbc42",
    "com.github.tototoshi" %% "slick-joda-mapper" % "2.0.0",
    specs2 % Test
  )
).enablePlugins(PlayScala).
  aggregate(clients.map(projectToRef): _*).
  dependsOn(sharedJvm)

lazy val client = (project in file("client")).settings(
  scalaVersion := scalaV,
  persistLauncher := true,
  persistLauncher in Test := false,
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.0",
    "com.lihaoyi" %%% "upickle" % "0.3.4",
    "com.greencatsoft" %%% "scalajs-angular" % "0.6"
  )
).enablePlugins(ScalaJSPlugin, ScalaJSPlay).
  dependsOn(sharedJs)

lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared")).
  settings(scalaVersion := scalaV).
  jvmSettings(
    libraryDependencies += "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided"
  )
  .jsSettings()
  .jsConfigure(_ enablePlugins ScalaJSPlay)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// loads the Play project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

routesGenerator := InjectedRoutesGenerator

cancelable in Global := true