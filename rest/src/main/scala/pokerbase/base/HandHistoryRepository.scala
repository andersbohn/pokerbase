package pokerbase.base

import pokerbase.model.HandHistory

import io.getquill._

import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

object HandHistoryRepository {

  val cpb = new CassandraAsyncContext[SnakeCase]("cpb_ctx")

  def storeHandHistory(idHand: Long, handHistory: HandHistory): Future[Unit] = {
    import cpb._
    val store = quote {
      query[HandHistory].insert(lift(handHistory))
    }
    run(store)
  }
}
