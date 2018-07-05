name := """play-java-fileupload-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice

libraryDependencies += "com.h2database" % "h2" % "1.4.197"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.358"

testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")
