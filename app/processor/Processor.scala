package processor

import akka.actor.{ActorLogging, Actor}
import model.{JsonHandHistory, ParsedHandHistory, HandHistory}
import play.api.libs.json._
import play.Logger._


class Processor extends Actor with ActorLogging {

  def receive = {
    case HandHistory(id, owner, source, timestamp, raw) =>
      warn("acted on " + id)

    case JsonHandHistory(owner, JsObject(json)) =>
      val x = json.find(_._1 == "raw").headOption.map {
        raw =>
          info(s"parsing json hh for $owner")
          val str = raw._2.as[String].trim

          val s = "\n"
          val ix = str.indexOf(s)
          if (ix > 0) {
            val header = str.substring(0, ix)
            if (header.startsWith("PokerStars Hand")) {
              if (header.contains("Tournament")) {
                debug("yey - tournament")
              } else {
                val all = processor.parsers.PokerStars.parseAll(processor.parsers.PokerStars.parser, str)

                // BULP: errors should be marked and followed up on regularly - might even hire a VA in china to be real vigilant about it
                if (all.isEmpty) {
                  warn("error parsing hh " + json.find(_._1 == "raw").mkString)
                } else {
                  ParsedHandHistory.insert(all.map(_.copy(owner = Some(owner))).get)

                }


              }
            }
          } else {
            warn("no NL??? >" + ix + "< " + str.indexOf("Seat"))
            warn("no NL in raw str? >" + str + "< ")
          }

          raw._2
      }.getOrElse("**none**")
      debug("acted on " + x)

    case x =>
      warn("elseX " + x)
  }

}
