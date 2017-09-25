name := "scala-spark"

version := "0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val sparkVer = "2.2.0"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVer withSources(),
    "org.apache.spark" %% "spark-streaming" % sparkVer,
    "org.apache.spark" %% "spark-sql" % sparkVer,
    "org.apache.spark" %% "spark-graphx" % sparkVer
  )
}

spDependencies += "graphframes/graphframes:0.5.0-spark2.1-s_2.11"

libraryDependencies ++= {
  val akkaVer = "10.0.10"
  Seq(
    "com.typesafe.akka" %% "akka-http" % akkaVer,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaVer,
    // Only when running against Akka 2.5 explicitly depend on akka-streams in same version as akka-actor
    "com.typesafe.akka" %% "akka-stream" % "2.5.4",
    "com.typesafe.akka" %% "akka-actor"  % "2.5.4"
  )
}