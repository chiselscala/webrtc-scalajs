import sbt.Keys._
import sbt.Project.projectToRef

name := "play-websocket-scala"
version := "1.0-SNAPSHOT"

JsEngineKeys.engineType := JsEngineKeys.EngineType.Node

// lazy val dockerRepo: String = sys.props.getOrElse("DOCKER_REPO", s"bpf.docker.repo")


// forced TERM else create problem for sbt old version 

(crossProject.crossType(CrossType.Pure) in file("shared"))
  // snip
  .jsSettings(name := "sharedJS")
  .jvmSettings(name := "sharedJVM")

lazy val sharedJVM = shared.jvm
lazy val sharedJS = shared.js


// lazy val shared = (crossProject.crossType(CrossType.Pure) in file("shared"))
//   .settings(
//     scalaVersion := Settings.versions.scala,
//     libraryDependencies ++= Settings.sharedDependencies.value
//   )
//   // set up settings specific to the JS project
//   .jsConfigure(_ enablePlugins ScalaJSWeb)

// lazy val sharedJVM = shared.jvm.settings(name := "sharedJVM")

// lazy val sharedJS = shared.js.settings(name := "sharedJS")

// use eliding to drop some debug code in the production build
lazy val elideOptions = settingKey[Seq[String]]("Set limit for elidable functions")

// instantiate the JS project for SBT with some additional settings
lazy val client: Project = (project in file("client"))
  .settings(
    name := "client",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    // libraryDependencies ++= Settings.scalajsDependencies.value,
    // by default we do development build, no eliding
    elideOptions := Seq(),
    scalacOptions ++= elideOptions.value,
    // jsDependencies ++= Settings.jsDependencies.value,
    // RuntimeDOM is needed for tests
    // jsDependencies += RuntimeDOM % "test",
    // yes, we want to package JS dependencies
    skip in packageJSDependencies := false,
    // use Scala.js provided launcher code to start the client app
    // persistLauncher := true,
    // persistLauncher in Test := false,
    // use uTest framework for tests
    testFrameworks += new TestFramework("utest.runner.Framework")
  )
  .enablePlugins(JSDependenciesPlugin, ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(sharedJS)

// Client projects (just one in this case)
lazy val clients = Seq(client)

// instantiate the JVM project for SBT with some additional settings
lazy val server = (project in file("server"))
  .settings(
    name := "server",
    version := Settings.version,
    scalaVersion := Settings.versions.scala,
    scalacOptions ++= Settings.scalacOptions,
    libraryDependencies ++= Settings.jvmDependencies.value,
    commands += ReleaseCmd,
    // triggers scalaJSPipeline when using compile or continuous compilation
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value,
    // connect to the client project
    scalaJSProjects := clients,
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    // compress CSS
    // LessKeys.compress in Assets := true
  )
  .enablePlugins(PlayScala)
  .disablePlugins(PlayLayoutPlugin) // use the standard directory layout instead of Play's custom
  .aggregate(clients.map(projectToRef): _*)
  .dependsOn(sharedJVM)

// Command for building a release
lazy val ReleaseCmd = Command.command("release") {
  state => "set elideOptions in client := Seq(\"-Xelide-below\", \"WARNING\")" ::
    "client/clean" ::
    "client/test" ::
    "server/clean" ::
    "server/test" ::
    "server/dist" ::
    "set elideOptions in client := Seq()" ::
    state
}

// loads the Play server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

