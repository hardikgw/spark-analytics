package main.scala

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.model.headers.{HttpOriginRange, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.server.{Directives, Route}
import spray.json._

import scala.util.parsing.json._
import scala.io.Source.fromFile

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol


class SparkService extends Directives with JsonSupport {
  val route: Route = respondWithHeader(`Access-Control-Allow-Origin`.*){
    get {
      pathPrefix("graphframe") {
        pathPrefix("e") {
          pathEndOrSingleSlash {
            complete(HttpEntity(ContentTypes.`application/json`, IndexerMain.getGraphFrame.toJson.toString()))
          }
        } ~
          pathPrefix("v") {
            pathEndOrSingleSlash {
              complete(IndexerMain.getGraphFrameVertices)
            }
          } ~ {
          pathPrefix("a") {
            pathEndOrSingleSlash {
              val jsonString = fromFile("src/main/resources/results.json").getLines.mkString.replaceAll("\\\\", "")
              complete(HttpEntity(ContentTypes.`application/json`, jsonString))
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