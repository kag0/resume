name := """resume"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  cache,
  javaWs,
  "org.projectlombok" % "lombok" % "1.16.6",
  "black.door" % "hate" % "v1r2t3",
  "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % "2.7.2",
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.7.2",
  "com.mashape.unirest" % "unirest-java" % "1.4.8",
  "org.javatuples" % "javatuples" % "1.2"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
