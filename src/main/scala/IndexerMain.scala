package main.scala

import org.apache.spark.sql.{DataFrame, Row, SparkSession}
import org.apache.spark.sql.types._
import org.apache.spark.{SparkConf, SparkContext}
import org.graphframes._

import scala.collection.immutable.HashMap

object IndexerMain {
  val file = "/Users/hp/workbench/projects/gmu/tweets/2017100309.txt"
//  val file = "src/main/resources/yagoFactInfluence.tsv"
//  val file = "hdfs://master:9000/data/yagoFactInfluence.tsv"
  def getSparkSession: SparkSession = {
//  val master = "spark://localhost:7077"
  val spark = SparkSession
    .builder()
    .appName("YAGO_Indexer")
    .config("spark.executor.memory", "3g")
    .config("spark.executor.instances", "2")
    .master("local")
    .getOrCreate()
  spark.sparkContext.setLogLevel("ERROR")
  spark
}

  def getGraphFrame: Map[String, Array[String]] = {
    val spark: SparkSession = getSparkSession
    val in = readRdfDf(spark, file)
    in.inDegrees.filter(_.size>1)
    val filtered = getTopGraphFrame(spark, in)
    val out: Map[String, Array[String]] = Map("edges" -> filtered.edges.toJSON.collect(),
      "vertices" -> filtered.vertices.toJSON.collect()
    )
    out
  }

  def getGraphFrameVertices: Array[String] = {
    val sc: SparkSession = getSparkSession
    val in = readRdfDf(sc, file)
    in.edges.toJSON.collect()
  }

  def getTopGraphFrame(spark: SparkSession, in: GraphFrame): GraphFrame = {

    in.edges.createOrReplaceTempView("e")
    in.vertices.createOrReplaceTempView("v")

    val topVertexIds = spark.sql("SELECT allview.ID as ID, count(*) as CNT FROM (SELECT src as ID FROM e UNION ALL SELECT dst as ID FROM e) as allview GROUP BY allview.ID")
    topVertexIds.filter(_.getLong(1) > 2).orderBy(topVertexIds.col("CNT").desc).limit(15).createOrReplaceTempView("f")

    val finalEdges = spark.sql("SELECT src, dst, attr FROM e WHERE e.src IN (SELECT ID FROM f) OR e.dst IN (SELECT ID FROM f)")
    finalEdges.createOrReplaceTempView("finalE")

    val finalVertices = spark.sql("SELECT id, attr FROM v WHERE v.id IN (SELECT src FROM finalE) OR v.id IN (SELECT dst from finalE)")

    GraphFrame(finalVertices, finalEdges)

  }

  def main(args: Array[String]) {

    val sc: SparkSession = getSparkSession

    val in = readRdfDf(sc, file)

    in.vertices.show()
    in.edges.show()

//    in.inDegrees.show()
//    in.outDegrees.show()

    in.edges.createOrReplaceTempView("e")
    in.vertices.createOrReplaceTempView("v")

    val topVertexIds = sc.sql("SELECT allview.ID as ID, count(*) as CNT FROM (SELECT src as ID FROM e UNION ALL SELECT dst as ID FROM e) as allview GROUP BY allview.ID")
    topVertexIds.filter(_.getLong(1) > 2).orderBy(topVertexIds.col("CNT").desc).limit(15).createOrReplaceTempView("f")

    val finalEdges = sc.sql("SELECT src, dst, attr FROM e WHERE e.src IN (SELECT ID FROM f) OR e.dst IN (SELECT ID FROM f)")
    finalEdges.createOrReplaceTempView("finalE")

    val finalVertices = sc.sql("SELECT id, attr FROM v WHERE v.id IN (SELECT src FROM finalE) OR v.id IN (SELECT dst from finalE)")

    GraphFrame(finalVertices, finalEdges)
  }

  def readRdfDf(spark: SparkSession, filename: String): GraphFrame = {
    val sc: SparkContext = spark.sparkContext

    val r = sc.textFile(filename).filter(_.matches("^.*(_hashtags_text|mentions_id).*$")).map(_.split('|')).filter(_.length>2)
    val v = r.map(_(0)).union(r.map(_(2))).distinct.zipWithIndex.map(
      x => Row(x._2.toString, x._1.toString))

    val stv = StructType(StructField("id", StringType) :: StructField("attr", StringType) :: Nil)

    val vdf = spark.createDataFrame(v, stv)

    vdf.createOrReplaceTempView("v")

    val str = StructType(
      StructField("subject", StringType) ::
      StructField("predicate", StringType) ::
      StructField("object", StringType) ::
        Nil
    )

    spark.createDataFrame(r.map(Row.fromSeq(_)), str)
      .createOrReplaceTempView("r")

    val edf = spark.sql("SELECT vsubject.id AS src, vobject.id AS dst, predicate AS attr FROM r JOIN v AS vsubject ON r.subject=vsubject.attr JOIN v AS vobject ON r.object=vobject.attr")

    edf.foreach((f) => {
//      println(f)
    })
    GraphFrame(vdf, edf)

  }

  def initContextAndCreateGF: GraphFrame = {
    val spark: SparkSession = getSparkSession
    val in = readRdfDf(spark, file)
    in.persist()
  }

}
