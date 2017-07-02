package pokerbase.base

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import pokerbase.model.HandHistory
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import pokerbase.base.api.ApiResponse

trait JsonSupport extends  {


}

object Routes extends SprayJsonSupport {

  import pokerbase.model.ModelJsonProtocol._

  implicit val jsonApiResponse = jsonFormat1(ApiResponse)

  val route =
    path("status") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Ok</h1>"))
      }
    } ~ path("handhistory" / LongNumber) { idHand =>
      put {
        entity(as[HandHistory]) { hh =>
          val saved = HandHistoryController.storeHandHistory(idHand, hh)
          onComplete(saved) { done =>
            complete(ApiResponse("ok"))
          }
        }
      }
    }


}
