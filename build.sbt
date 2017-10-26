name := "scala-spark"

version := "0.1"

scalaVersion := "2.11.8"

sparkVersion := "2.2.0"

val provided = "compile"

libraryDependencies ++= {
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion.value % provided,
    "org.apache.spark" %% "spark-streaming" % sparkVersion.value % provided,
    "org.apache.spark" %% "spark-sql" % sparkVersion.value % provided,
    "org.apache.spark" %% "spark-graphx" % sparkVersion.value % provided
  )
}

spDependencies += "graphframes/graphframes:0.5.0-spark2.1-s_2.11"

sparkComponents ++=  Seq("core", "streaming", "sql", "graphx")


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