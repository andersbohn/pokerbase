package model

import play.api.libs.json.{Json, JsObject, JsValue}
import scala.concurrent.Future
import domain._


case class ParsedHandHistory(table: Table, header: Header, actions: List[Action])

object ParsedHandHistory extends MongoCollection {

  def name = "parsedhandhistories"

  import scala.concurrent.ExecutionContext.Implicits.global

  def insert(value: JsValue) = {
    collection.insert(value)
  }

  def insert(parsedHandHistory: ParsedHandHistory) = {


    //    implicit val formatParsedHandHistory = Json.format[ParsedHandHistory]
    //    import spray.json._
    //    import DefaultJsonProtocol._
    //
    //    collection.insert(parsedHandHistory.toJson)
    ""
  }


  def findById(id: String): Future[Option[JsObject]] = {

    //    val fields = Json.obj("raw" -> id)
    val query: JsObject = Json.obj("id" -> id)
    val futureOptionJsObject: Future[Option[JsObject]] = collection.find(query).one[JsObject]

    futureOptionJsObject
  }

}

