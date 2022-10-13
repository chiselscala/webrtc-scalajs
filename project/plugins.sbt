// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += Resolver.typesafeRepo("releases")

// find version from https://index.scala-lang.org/vmunier/sbt-web-scalajs/artifacts/sbt-web-scalajs

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.17")

// addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-coffeescript" % "1.0.2")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.5.1")

addSbtPlugin("com.github.sbt" % "sbt-native-packager" % "1.9.8")

addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % "1.0.2")

addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.2.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.3")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.2")

addSbtPlugin("org.scala-js" % "sbt-jsdependencies" % "1.0.2")





// addSbtPlugin("com.vmunier"               % "sbt-web-scalajs"           % "1.2.0")
// addSbtPlugin("org.scala-js"              % "sbt-scalajs"               % "1.10.0")
// addSbtPlugin("com.typesafe.play"         % "sbt-plugin"                % "2.8.13")
// addSbtPlugin("org.portable-scala"        % "sbt-scalajs-crossproject"  % "1.2.0")
// addSbtPlugin("com.typesafe.sbt"          % "sbt-gzip"                  % "1.0.2")
// addSbtPlugin("com.typesafe.sbt"          % "sbt-digest"                % "1.1.4")
