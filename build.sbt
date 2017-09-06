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
//    "graphframes/graphframes:0.5.0-spark2.1-s_2.11"
  )
}

spDependencies += "graphframes/graphframes:0.5.0-spark2.1-s_2.11"

//resolvers += Resolver.url("SparkPackages", url("https://dl.bintray.com/spark-packages/maven/"))