package pokerbase.client

object RunPgHhExporter extends App {

  val res = PgHhExporter.streamHandsFromPg(Some(1), Some(50))

  res.foreach { hh => println(s" - ${hh.handId} - ${hh.handHistory.take(20)}..") }

}
