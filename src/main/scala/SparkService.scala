package main.scala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import main.scala.IndexerMain
import spray.json._

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol


class SparkService extends Directives with JsonSupport {
  val route: Route =
    get {
      pathPrefix("graphframe") {
        pathPrefix("e") {
          pathEndOrSingleSlash {
            complete(IndexerMain.getGraphFrameEdges)
          }
        } ~
          pathPrefix("v") {
            pathEndOrSingleSlash {
              complete(IndexerMain.getGraphFrameVertices)
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