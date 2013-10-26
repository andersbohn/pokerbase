package model

import play.api.libs.json.{Json, JsObject, JsValue}
import scala.concurrent.Future
import domain._
import reactivemongo.core.commands.LastError
import reactivemongo.api.Cursor


case class ParsedHandHistory(handId: String, playerId: String, table: Table, header: Header, actions: List[Action])

//, summaries: List[SeatSummary])

object ParsedHandHistory extends MongoCollection {

  def name = "parsedhandhistories"

  import scala.concurrent.ExecutionContext.Implicits.global

  def insert(value: JsValue) = {
    collection.insert(value)
  }

  def insert(parsedHandHistory: ParsedHandHistory): Future[LastError] = {

    import JsonFormats._
    implicit val formatParsedHandHistory = Json.format[ParsedHandHistory]

    collection.insert(Json.toJson(parsedHandHistory))
  }


  def findById(id: String): Future[Option[JsObject]] = {

    //    val fields = Json.obj("raw" -> id)
    val query: JsObject = Json.obj("handId" -> id)
    val futureOptionJsObject: Future[Option[JsObject]] = collection.find(query).one[JsObject]

    futureOptionJsObject
  }

  def list: Future[List[JsObject]] = {

    val query = Json.obj()

    val cursor: Cursor[JsObject] = collection.find(query).cursor[JsObject]

    cursor.toList
  }

}

