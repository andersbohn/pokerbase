package controllers

import model.{ParsedHandHistory, HandHistory}

import play.api.mvc._
import play.modules.reactivemongo.MongoController
import reactivemongo.core.commands.LastError
import scala.concurrent.Future
import akka.actor.Props
import processor.Processor
import java.util.UUID
import play.libs.Akka


object HandHistories extends Controller with MongoController {

  def getHandHistory(id: String) = Action {
    Async {
      HandHistory.findById(id).map(_.map(Ok(_)).getOrElse(NotFound))
    }
  }

  def getParsedHandHistory(id: String) = Action {
    Async {
      ParsedHandHistory.findById(id).map(_.map(Ok(_)).getOrElse(NotFound))
    }
  }

  def createFromJson = Action(parse.json) {
    request =>
      Async {
        println("calling insert ")
        val insert: Future[LastError] = HandHistory.insert(request.body)
        println("insert started " + insert.isCompleted)
        insert.onComplete {
          hh =>
            println("completed insert - akkaing - " + hh)

            val system = Akka.system

            println("akkaing - system " + system)

            val processor = system.actorOf(Props[Processor], name = "processorActor" + UUID.randomUUID().toString)

            println("akkaing - system " + processor)

            processor ! request.body

            println("akkaing - sent hh!")

        }
        insert.map {
          lastError =>
          // TODO kickstart actor processor..
            println("oking")
            Ok("Mongo LastError:%s".format(lastError))
        }
      }
  }
}
