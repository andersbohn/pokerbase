package model

import play.api.libs.json.{Json, JsObject, JsValue}
import scala.concurrent.Future
import domain._
import reactivemongo.core.commands.LastError
import reactivemongo.api.Cursor
import play.Logger._

case class ParsedHandHistory(handId: String, playerId: String, owner:Option[String], table: Table, header: Header, actions: List[Action])

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
    info(s"inserting parsedhh ${parsedHandHistory.handId} for ${parsedHandHistory.owner}")
    collection.insert(Json.toJson(parsedHandHistory))
  }


  def findById(id: String): Future[Option[JsObject]] = {

    //    val fields = Json.obj("raw" -> id)
    val query: JsObject = Json.obj("handId" -> id)
    val futureOptionJsObject: Future[Option[JsObject]] = collection.find(query).one[JsObject]

    futureOptionJsObject
  }

  def list(owner:String): Future[List[JsObject]] = {

    val query: JsObject = Json.obj("owner" -> owner)

    val cursor: Cursor[JsObject] = collection.find(query).cursor[JsObject]

    cursor.toList
  }

}

