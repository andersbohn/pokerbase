package model

import java.util.Date
import scala.concurrent.Future
import play.api.libs.json.{JsValue, Json, JsObject}


sealed case class Site(id: String, name: String)

//case object PokerStars extends Site("PS", "PokerStars")

case class HandHistory(id: String, source: String, timestamp: Date, raw: String)

object HandHistory extends MongoCollection {

  def name = "handhistories"

  import scala.concurrent.ExecutionContext.Implicits.global

  def insert(value: JsValue) = {
    collection.insert(value)
  }

  def findById(id: String): Future[Option[JsObject]] = {

    //    val fields = Json.obj("raw" -> id)
    val query: JsObject = Json.obj("id" -> id)
    val futureOptionJsObject: Future[Option[JsObject]] = collection.find(query).one[JsObject]

    futureOptionJsObject
  }

}