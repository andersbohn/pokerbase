package pokerbase.model

import spray.json._

object ModelJsonProtocol extends DefaultJsonProtocol {

  implicit val jsonHandHistory = jsonFormat2(HandHistory)

}
