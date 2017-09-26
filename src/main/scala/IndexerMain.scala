package main.scala

import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}
import org.graphframes._

import scala.collection.immutable.HashMap

object IndexerMain {

//  val file = "src/main/resources/yagoFactInfluence.tsv"
  val file = "hdfs://master:9000/data/yagoFactInfluence.tsv"
  def getContext: SparkContext = {
    val master = "spark://localhost:7077"
//    val conf = new SparkConf().setMaster("local").setAppName("YAGO_Indexer").set("spark.executor.memory", "3g").set("spark.executor.instances", "2")
    val conf = new SparkConf().setMaster(master).setAppName("YAGO_Indexer").set("spark.executor.memory", "2g").setJars(Seq("target/scala-2.11/scala-spark_2.11-0.1.jar"))
    val sc = new SparkContext(conf)
    sc.setLogLevel("ERROR")
    sc
  }

  def getGraphFrame: Map[String, Array[String]] = {
    val sc: SparkContext = getContext
    val in = readRdfDf(sc, file)
    val out: Map[String, Array[String]] = Map("edges" -> in.edges.toJSON.collect(),
    "vertices" -> in.vertices.toJSON.collect()
    )
    out
  }

  def getGraphFrameVertices: Array[String] = {
    val sc: SparkContext = getContext
    val in = readRdfDf(sc, file)
    in.edges.toJSON.collect()
  }
  def main(args: Array[String]) {
    val sc: SparkContext = getContext

    val in = readRdfDf(sc, file)

    in.vertices.show()

    in.vertices.foreach((f) => {
      println(f)
    })
    in.edges.foreach((f) => {
      println(f)
    })

    in.inDegrees.show()
    in.outDegrees.show()

    in.edges.createOrReplaceTempView("e")
    in.vertices.createOrReplaceTempView("v")
    val in2 = GraphFrame(in.vertices.sqlContext.sql(
      "SELECT v.id," +
        "       FIRST(v.attr) AS attr," +
        "       COUNT(*) AS outdegree " +
        "FROM   v " +
        "JOIN   e " +
        "  ON   v.id=e.src " +
        "GROUP BY v.id").cache,
      in.edges)

    val absent = in2.find("(v1)-[]->(v2); (v2)-[]->(v3); !(v1)-[]->(v3)")
    absent.createOrReplaceTempView("a")

    val present = in2.find("(v1)-[]->(v2); (v2)-[]->(v3); (v1)-[]->(v3)")
    present.createOrReplaceTempView("p")

    absent.sqlContext.sql(
      "SELECT v1 an," +
        "       SUM(v1.outdegree * v2.outdegree * v3.outdegree) AS ac " +
        "FROM   a " +
        "GROUP BY v1").createOrReplaceTempView("aa")

    present.sqlContext.sql(
      "SELECT v1 pn," +
        "       SUM(v1.outdegree * v2.outdegree * v3.outdegree) AS pc " +
        "FROM   p " +
        "GROUP BY v1").createOrReplaceTempView("pa")

    absent.sqlContext.sql("SELECT an," +
      "       ac * pc/(ac+pc) AS score " +
      "FROM   aa " +
      "JOIN   pa" +
      "  ON   an=pn " +
      "ORDER BY score DESC").show
  }

  def readRdfDf(sc: org.apache.spark.SparkContext, filename: String): GraphFrame = {
    val r = sc.textFile(filename).map(_.split("\t"))
    val v = r.map(_ (1)).union(r.map(_ (3))).distinct.zipWithIndex.map(
      x => Row(x._2, x._1))
    // We must have an "id" column in the vertices DataFrame;
    // everything else is just properties we assign to the vertices
    val stv = StructType(StructField("id", LongType) ::
      StructField("attr", StringType) :: Nil)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    val vdf = sqlContext.createDataFrame(v, stv)
    vdf.createOrReplaceTempView("v")
    val str = StructType(StructField("rdfId", StringType) ::
      StructField("subject", StringType) ::
      StructField("predicate", StringType) ::
      StructField("object", StringType) :: Nil)
    sqlContext.createDataFrame(r.map(Row.fromSeq(_)), str)
      .createOrReplaceTempView("r")
    // We must have an "src" and "dst" columns in the edges DataFrame;
    // everything else is just properties we assign to the edges
    val edf = sqlContext.sql("SELECT vsubject.id AS src, vobject.id AS dst, predicate AS attr FROM   r JOIN   v AS vsubject  ON   subject=vsubject.attr JOIN   v AS vobject  ON   object=vobject.attr")
    GraphFrame(vdf, edf)

  }

  def initContextAndCreateGF: GraphFrame = {
    val sc: SparkContext = getContext
    val in = readRdfDf(sc, file)
    in.persist()
  }

}
