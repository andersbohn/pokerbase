package pokerbase.client

import io.getquill._
import pokerbase.model.HandHistory

object PgHhExporter {

  object Schema {
    lazy val ctx = new PostgresJdbcContext[SnakeCase]("ctx")
  }

  case class TourneyHandHistories(idHand: String, history: String) {
    def toHandHistory = HandHistory(idHand, history)

  }

  def streamHandsFromPg(startAtPkId: Option[Long] = None, maxRows: Option[Long] = None): List[HandHistory] = {

    import Schema.ctx._
    val areas = quote {
      query[TourneyHandHistories]
    }
    run(areas).map(_.toHandHistory)
  }
  
}
