/** Name of project */
name := "ScalUber"

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
  "Search Maven" at "https://repo1.maven.org/maven2/",
)

/** Source Dependencies */
libraryDependencies ++= Seq(
  "io.circe" % "circe-core_2.12" % "0.9.0-M2",
  "io.circe" % "circe-generic_2.12" % "0.9.0-M2",
  "io.circe" % "circe-parser_2.12" % "0.9.0-M2",
  "org.scalaj" % "scalaj-http_2.12" % "2.3.0"
)

/** Make sure to fork on run */
fork in run := true