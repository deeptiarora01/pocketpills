name := """play-java-fileupload-example"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.6"

crossScalaVersions := Seq("2.11.12", "2.12.4")

libraryDependencies += guice

libraryDependencies += "com.h2database" % "h2" % "1.4.197"

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.358"

libraryDependencies += "io.findify" %% "s3mock" % "0.2.4" % "test"

testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-v")

excludeDependencies += "commons-logging" % "commons-logging"

mainClass in assembly := Some("play.core.server.ProdServerStart")
fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value)

assemblyMergeStrategy in assembly := {
  case manifest if manifest.contains("MANIFEST.MF") =>
    // We don't need manifest files since sbt-assembly will create
    // one with the given settings
    MergeStrategy.discard
  case referenceOverrides if referenceOverrides.contains("reference-overrides.conf") =>
    // Keep the content for all reference-overrides.conf files
    MergeStrategy.concat
  case PathList("META-INF", "io.netty.versions.properties", xs @ _*) => MergeStrategy.last
  case x =>
    // For all the other files, use the default sbt-assembly merge strategy
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}