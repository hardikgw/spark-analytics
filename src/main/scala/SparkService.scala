package main.scala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.`Access-Control-Allow-Origin`
import akka.http.scaladsl.server.{Directives, Route}
import org.apache.spark.sql.{DataFrame, Dataset}
import spray.json._

import scala.io.Source.fromFile

final case class GraphItems(edges:Edges, vertices:Vertices)
final case class Edges(src: String, dst: String, attrs: Map[String, String])
final case class Vertices(id: String, attrs: Map[String, String])

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol{

}

class SparkService extends Directives with JsonSupport{

  val route: Route = respondWithHeader(`Access-Control-Allow-Origin`.*){
    get {
      pathPrefix("graphframe") {
        pathPrefix("e") {
          pathEndOrSingleSlash {
            complete(IndexerMain.getGraphFrame)
          }
        } ~
          pathPrefix("connected") {
            pathEndOrSingleSlash {
              parameter("nodeId") { (nodeId) =>
                complete(IndexerMain.getLinkedNodes(nodeId))
              }
            }
          } ~ {
          pathPrefix("a") {
            pathEndOrSingleSlash {
              val jsonString = fromFile("src/main/resources/results.json").getLines.mkString.replaceAll("\\\\", "")
              complete(jsonString)
            }
          }
        }

      }
    } ~ {
      pathPrefix("") {
        pathEndOrSingleSlash {
          getFromFile("src/main/resources/dist/index.html")
        } ~
          getFromDirectory("src/main/resources/dist")
      }
    }
  }
}