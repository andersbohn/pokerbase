package processor

import akka.actor.{ActorLogging, Actor}
import model.HandHistory
import play.api.libs.json._

class Processor extends Actor with ActorLogging {

  def receive = {
    case HandHistory(id, source, timestamp, raw) =>
      println("acted on " + id)

    case JsObject(json) =>
      val x = json.find(_._1 == "raw").headOption.map {
        raw =>

        // PS: tour header - PokerStars Hand #103171133087: Tournament #775135132, $15.00+$1.50 USD Hold'em No Limit - Level II (15/30) - 2013/08/24 19:36:26 CET [2013/08/24 13:36:26 ET]
        // PS: cash nl header - PokerStars Hand #103171854335:  Hold'em No Limit ($0.10/$0.25 USD) - 2013/08/24 19:53:52 CET [2013/08/24 13:53:52 ET]
        // Note: as[String] automatically converts the \n's to real newlines...
          val str = raw._2.as[String].trim

          val s = "\n"
          val ix = str.indexOf(s)
          if (ix > 0) {
            val header = str.substring(0, ix)
            if (header.startsWith("PokerStars Hand")) {
              if (header.contains("Tournament")) {
                println("yey - tournament")
              } else {
                println("ok - cashgame")
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
