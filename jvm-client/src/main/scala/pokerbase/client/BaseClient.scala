package pokerbase.client

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import org.slf4s.Logging
import pokerbase.model.HandHistory

import scala.concurrent.Future


class BaseClient extends Logging {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import spray.json._
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import pokerbase.model.ModelJsonProtocol._

  def putHandHistory(handId: Long, hh: HandHistory): Future[Boolean] = {

    val json = HttpEntity(ContentTypes.`application/json`, hh.toJson.prettyPrint)
    Http().singleRequest(HttpRequest(uri = "http://localhost:8080/handhistory/" + handId, method = HttpMethods.PUT, entity = json)).map { r =>
      log.debug(s"put handhistory $handId -> ${r.status}")
      r.status.isSuccess()
    }

  }

  def shutdown: Unit = {
    system.terminate()
  }

}
