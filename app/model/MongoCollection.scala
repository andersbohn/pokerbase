package model

import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.api.{MongoConnection, MongoDriver}

trait MongoCollection {

  def name: String

  val driver = new MongoDriver
  val connection: MongoConnection = driver.connection(List("localhost"))

  def collection = {
    import reactivemongo.api._
    import scala.concurrent.ExecutionContext.Implicits.global

    val db: DefaultDB = connection("pokerbase")

    db.collection[JSONCollection](name)
  }

}
