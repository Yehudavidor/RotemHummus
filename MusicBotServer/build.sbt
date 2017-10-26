name := """play-java-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += "com.google.code.gson" % "gson" % "2.8.1"
libraryDependencies += "org.apache.commons" % "commons-collections4" % "4.1"
libraryDependencies += "org.apache.commons" % "commons-collections4" % "4.1"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "RELEASE"
libraryDependencies += "org.apache.httpcomponents" % "httpcore" % "4.4.6"
libraryDependencies += "org.apache.httpcomponents" % "httpclient" % "4.5.3"
libraryDependencies += "ai.api" % "libai" % "1.4.8"
libraryDependencies += "org.json" % "json" % "20170516"