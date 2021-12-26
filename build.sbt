ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "Akka Assignment"
  )
lazy val AkkaVersion = "2.6.17"
libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion