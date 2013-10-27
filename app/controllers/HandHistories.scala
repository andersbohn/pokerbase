package controllers

import model._

import play.api.mvc._
import play.modules.reactivemongo.MongoController
import reactivemongo.core.commands.LastError
import scala.concurrent.Future
import akka.actor.Props
import processor.Processor
import java.util.UUID
import play.libs.Akka
import play.api.libs.json._
import jp.t2v.lab.play2.auth.AuthElement
import play.Logger
import Logger.info
import model.JsonHandHistory
import play.api.libs.json.JsObject

object HandHistories extends Controller with MongoController with AuthElement with AuthConfigImpl with Security {


  def getHandHistory(id: String) = authorizedAction(NormalUser) {
    user => implicit request =>
      Async {
        HandHistory.findById(id).map(_.map {
          json =>
            val docOwner = (json \ "owner").asOpt[String]
            docOwner.filter(_ == user.name).map(Ok(_)).getOrElse(Forbidden)
        }.getOrElse(NotFound))
      }
  }

  def listHandHistories = authorizedAction(NormalUser) {
    user => implicit request =>
      Async {
        val list: Future[List[JsObject]] = HandHistory.listFor(user.name)
        list.map {
          hhs =>
            Logger.info(s"list: $hhs")
            Ok(Json.toJson(hhs))
        }
      }
  }

  def listParsedHandHistory = authorizedAction(NormalUser) {
    user => implicit request =>
      Async {
        val list: Future[List[JsObject]] = ParsedHandHistory.list(user.name)
        list.map {
          hhs =>
            Logger.info(s"list: $hhs")
            Ok(Json.toJson(hhs))
        }
      }
  }

  def getParsedHandHistory(id: String) = Action {
    Async {
      info(s"id: $id")
      ParsedHandHistory.findById(id).map(_.map(Ok(_)).getOrElse(NotFound))
    }
  }

  def put = AuthenticatedP(play.api.mvc.BodyParsers.parse.json) {
    // authorizedAction(parse.json, NormalUser) {
    implicit request =>


      Async {
        info(s"calling insert for ${request.user}")
        val insert: Future[LastError] = HandHistory.insert(request.body)
        info(s"insert started ${insert.isCompleted}")
        insert.onComplete {
          hh =>
            info(s"completed insert - akkaing $hh")

            val system = Akka.system

            info(s"akkaing - system $system")

            val processor = system.actorOf(Props[Processor], name = "processorActor" + UUID.randomUUID().toString)

            info(s"akkaing - system $processor")

            processor ! JsonHandHistory(request.user.name, request.body)

            info(s"akkaing - sent hh!")

        }
        insert.map {
          lastError =>
          // TODO kickstart actor processor..
            info(s"oking")
            Ok("Mongo LastError:%s".format(lastError))
        }
      }
  }
}
