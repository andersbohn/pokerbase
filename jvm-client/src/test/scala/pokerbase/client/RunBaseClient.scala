package pokerbase.client

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.io.StdIn

object RunBaseClient extends App {

  import scala.concurrent.ExecutionContext.Implicits.global

  val res = PgHhExporter.streamHandsFromPg(Some(1), Some(50))

  res.take(5).foreach { hh => println(s" - ${hh.handId} - ${hh.handHistory.take(20)}..") }

  val bc = new BaseClient

  bc.putHandHistory(res.head.handId.toLong, res.head).onComplete { t =>
    println(s"R $t")

    bc.shutdown
    sys.exit()
  }

}
