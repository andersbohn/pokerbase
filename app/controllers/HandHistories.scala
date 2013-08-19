package controllers

import model.HandHistory

import play.api.mvc._
import play.modules.reactivemongo.MongoController
import reactivemongo.bson.BSONDocument
import scala.concurrent.Future


object HandHistories extends Controller with MongoController {

  def get(id: String) = Action {
    Async {
      val futureHandHistoryOption: Future[Option[BSONDocument]] = HandHistory.find(id)
      futureHandHistoryOption.map {
        doc => Ok(doc.map(BSONDocument.pretty).getOrElse(""))
      }
    }
  }

}
