name := "gtw-preprocessRDF"

version := "0.1"

scalaVersion := "2.11.11"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.4.0" % "provided",
  "org.apache.spark" %% "spark-graphx" % "2.4.0" % "provided",
//  "org.apache.spark" %% "spark-core" % "2.4.0",
//  "org.apache.spark" %% "spark-sql" % "2.4.0",
//  "org.apache.spark" %% "spark-graphx" % "2.4.0",
  "com.github.scopt" %% "scopt" % "3.5.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"
)

assemblyJarName in assembly := "gtw-preprocessRDF.jar"
