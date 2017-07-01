package pokerbase.client

import java.sql.DriverManager

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

  def jdbcStreamHandsFromPg(startAtPkId: Option[Long] = None, maxRows: Option[Long] = None): List[HandHistory] = {
    Class.forName("org.postgresql.Driver")
    val url = "jdbc:postgresql://localhost:5432/PT4 DB"

    val furl = url + "?user=postgres&password=dbpass"
    val conn = DriverManager.getConnection(furl)
    val where = Seq(startAtPkId.map(? => " id_hand > ? ")).flatten

    val sql = "select * from tourney_hand_histories " +
      (if (where.nonEmpty) " where " + where.mkString(" and ") else "") +
      maxRows.map(" limit " + _).getOrElse("")

    println(s"sql $sql")

    val stmt = conn.prepareStatement(sql)
    try {
      var ix = 1
      startAtPkId.foreach { pk =>
        stmt.setLong(ix, pk)
        ix += 1
      }

      val rs = stmt.executeQuery()
      var hists = List.empty[HandHistory]
      while (rs.next()) {
        val handId = rs.getString("id_hand")
        val history = rs.getString("history")
        hists = HandHistory(handId, history) :: hists
      }
      hists
    } finally {
      conn.close()
    }
  }

}
