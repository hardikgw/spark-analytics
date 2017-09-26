package main.scala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.headers.{HttpOriginRange, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server.{Directives, Route}
import spray.json._

import scala.io.Source.fromFile

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol


class SparkService extends Directives with JsonSupport {
  val route: Route = respondWithHeader(`Access-Control-Allow-Origin`.*){
    get {
      pathPrefix("graphframe") {
        pathPrefix("e") {
          pathEndOrSingleSlash {
            complete(IndexerMain.getGraphFrame)
          }
        } ~
          pathPrefix("v") {
            pathEndOrSingleSlash {
              complete(IndexerMain.getGraphFrameVertices)
            }
          } ~ {
          pathPrefix("a") {
            pathEndOrSingleSlash {
              complete(fromFile("src/main/resources/results.json").getLines.mkString)
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