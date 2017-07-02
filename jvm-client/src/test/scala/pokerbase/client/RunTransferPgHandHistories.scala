package pokerbase.client

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

object RunTransferPgHandHistories extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  val res = PgHhExporter.streamHandsFromPg(None, None)

  val bc = new BaseClient
  val results = res.map { hh =>
    bc.putHandHistory(hh.handId.toLong, hh).map ( hh.handId -> _ )
  }
  val r = Await.result(Future.sequence(results), 10 seconds)

  val (ok, failed) = r.partition(_._2)
  println(s"R ${ok.size} failed: ${failed.size}")

  System.in.read()

  bc.shutdown

}
