enablePlugins(ScalaJSPlugin)
scalaJSUseMainModuleInitializer := true

/** Name of project */
name := "Scalllobarator"

/** Organization */
organization := "com.github.sguzman"

/** Project Version */
version := "1.0"

/** Scala version */
scalaVersion := "2.12.4"

/** Scalac parameters */
scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-encoding", "utf8")

/** Javac parameters */
javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-server")

/** Resolver */
resolvers ++= Seq(
  DefaultMavenRepository,
  Resolver.sonatypeRepo("public"),
  Resolver.typesafeRepo("releases"),
  Resolver.sbtPluginRepo("releases"),
  Resolver.jcenterRepo,
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
)

/** Source Dependencies */
libraryDependencies ++= Seq(
  "io.circe" %%% "circe-core" % "0.9.0-M2",
  "io.circe" %%% "circe-generic" % "0.9.0-M2",
  "io.circe" %%% "circe-parser" % "0.9.0-M2",
  "fr.hmil" %%% "roshttp" % "2.0.2",
  "org.scala-js" %%% "scalajs-dom" % "0.9.1",
  "io.surfkit" %%% "scalajs-google-maps" % "0.0.3-SNAPSHOT"
)

/** Make sure to fork on run */
fork in run := true