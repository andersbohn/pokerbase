package model

import play.modules.reactivemongo.json.collection.JSONCollection

trait MongoCollection {

  def name: String

  def collection = {
    import reactivemongo.api._
    import scala.concurrent.ExecutionContext.Implicits.global

    val driver = new MongoDriver
    val connection: MongoConnection = driver.connection(List("localhost"))

    val db: DefaultDB = connection("pokerbase")

    db.collection[JSONCollection](name)
  }

}
