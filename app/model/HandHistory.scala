package model

import java.util.Date
import reactivemongo.bson.BSONDocument
import reactivemongo.api.collections.default.BSONCollection
import scala.concurrent.Future


sealed case class Site(id: String, name: String)

//case object PokerStars extends Site("PS", "PokerStars")

case class HandHistory(id: String, source: String, timestamp: Date, raw: String)

object HandHistory {

  def find(id: String): Future[Option[BSONDocument]] = {

    import reactivemongo.api._
    import scala.concurrent.ExecutionContext.Implicits.global

    val driver = new MongoDriver
    val connection: MongoConnection = driver.connection(List("localhost"))

    val db: DefaultDB = connection("pokerbase")

    val collection: BSONCollection = db.apply("handhistories")

    val query = BSONDocument("id" -> id)

    val fields = BSONDocument("raw" -> 1)

    val future: Future[Option[BSONDocument]] = collection.find(query, fields).one[BSONDocument]
    future
  }


}