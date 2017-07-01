package processor.parsers

import spray.json._

object ParsedHandHistoryTestClient extends App {

  import pokerbase.domain.NLHEJsonProtocol._

  val name = "/Users/abj/Projects/pokerbase/test/json/sample-parsed-hand-history.json"

  val src = scala.io.Source.fromFile(name)
  val jsonString = src.mkString
  src.close()
  val json = jsonString.parseJson
  println("JSON -> " + json.prettyPrint)
  //    import scala.concurrent.ExecutionContext.Implicits.global
  //    val x = ParsedHandHistory.insert(json)

  //    x.onComplete {
  //      x =>
  //        println("done ! ok? : "+x.isSuccess)
  //    }
  //    Await.result(x, 50 seconds)


}
