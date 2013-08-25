package controllers

import model.HandHistory

import play.api.mvc._
import play.modules.reactivemongo.MongoController


object HandHistories extends Controller with MongoController {

  def get(id: String) = Action {
    Async {
      HandHistory.findById(id).map(_.map(Ok(_)).getOrElse(NotFound))
    }
  }

  def createFromJson = Action(parse.json) {
    request =>
      Async {
        HandHistory.insert(request.body).map(lastError =>
          Ok("Mongo LastErorr:%s".format(lastError)))
      }
  }
}
