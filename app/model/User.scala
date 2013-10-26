package model

import play.api.libs.json.{Json, JsObject}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import reactivemongo.core.commands.LastError
import play.Logger

case class User(name: String, salt: String, hash: String) {
  def permission = NormalUser
}

object User extends MongoCollection {

  def name = "users"

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val formatParsedHandHistory = Json.format[User]

  def insert(user: User) = collection.insert(Json.toJson(user))

  def findUserByName(name: String): Option[User] = {
    val result: Option[JsObject] = Await.result(findByName(name), 120 seconds)
    result.map {
      json => Json.fromJson(json).get
    }
  }

  def getByName(name: String): User = findUserByName(name).get


  def findByName(name: String): Future[Option[JsObject]] = {

    //    val fields = Json.obj("raw" -> id)
    val query: JsObject = Json.obj("name" -> name)

    val futureOptionJsObject: Future[Option[JsObject]] = collection.find(query).one[JsObject]

    futureOptionJsObject
  }

  // FIXME abj introduce password check!
  def authenticate(user: String, pwd: String): Option[User] = findUserByName(user)

  def createUser(user: String, pwd: String): Option[User] = {
    if (findUserByName(user).isDefined) {
      // TODO replace with some 'exists' lookup, that neednot convert to json...
      Logger.info("User " + user + "exists")
      None
    } else {
      val newUser = User(user, pwd, pwd)
      val result: LastError = Await.result(insert(newUser), 5 seconds)

      Logger.info(s"register user $newUser -> $result")

      if (result.ok) {
        Some(newUser)
      } else {
        None
      }
    }
  }


}

sealed trait Permission

case object Administrator extends Permission

case object NormalUser extends Permission