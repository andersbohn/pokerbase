package model

import java.util.Date
import scala.concurrent.Future
import play.api.libs.json.{JsValue, Json, JsObject}
import play.modules.reactivemongo.json.collection.JSONCollection


sealed case class Site(id: String, name: String)

//case object PokerStars extends Site("PS", "PokerStars")

case class HandHistory(id: String, source: String, timestamp: Date, raw: String)

object HandHistory {

  import scala.concurrent.ExecutionContext.Implicits.global

  def insert(value: JsValue) = {
    collection.insert(value)
  }


  def collection = {
    import reactivemongo.api._
    import scala.concurrent.ExecutionContext.Implicits.global

    val driver = new MongoDriver
    val connection: MongoConnection = driver.connection(List("localhost"))

    val db: DefaultDB = connection("pokerbase")

    db.collection[JSONCollection]("handhistories")
  }

  def findById(id: String): Future[Option[JsObject]] = {

//    val fields = Json.obj("raw" -> id)
    val query: JsObject = Json.obj("id" -> id)
    val futureOptionJsObject: Future[Option[JsObject]] = collection.find(query).one[JsObject]

    futureOptionJsObject
  }

}