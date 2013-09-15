package processor

import akka.actor.{ActorLogging, Actor}
import model.{ParsedHandHistory, HandHistory}
import play.api.libs.json._
import play.Logger

class Processor extends Actor with ActorLogging {

  def receive = {
    case HandHistory(id, source, timestamp, raw) =>
      println("acted on " + id)

    case JsObject(json) =>
      val x = json.find(_._1 == "raw").headOption.map {
        raw =>

          val str = raw._2.as[String].trim

          val s = "\n"
          val ix = str.indexOf(s)
          if (ix > 0) {
            val header = str.substring(0, ix)
            if (header.startsWith("PokerStars Hand")) {
              if (header.contains("Tournament")) {
                println("yey - tournament")
              } else {
                val all = processor.parsers.PokerStars.parseAll(processor.parsers.PokerStars.parser, str)

                // BULP: errors should be marked and followed up on regularly - might even hire a VA in china to be real vigilant about it
                if (all.isEmpty) {
                  Logger.warn("error parsing hh " + json.find(_._1 == "raw").mkString)
                } else {

                  ParsedHandHistory.insert(all.get)

                }


              }
            }
          } else {
            println("no NL??? >" + ix + "< " + str.indexOf("Seat"))
            println("no NL in raw str? >" + str + "< ")
          }

          raw._2
      }.getOrElse("**none**")
      println("acted on " + x)

    case x =>
      println("elseX " + x)
  }

}
