package processor.parsers

import org.junit.{Ignore, Test}
import model.ParsedHandHistory
import play.api.libs.json.Json
import scala.concurrent.Await
import scala.concurrent.duration._


class ParsedHandHistoryTestClient {


  @Test
  @Ignore
  def testLoadAllInFile {
    val name = "/Users/abj/Projects/pokerbase/test/json/sample-parsed-hand-history.json"

    val src = scala.io.Source.fromFile(name)
    val jsonString = src.mkString
    src.close()
    val json = Json.parse(jsonString)
    import scala.concurrent.ExecutionContext.Implicits.global
    val x = ParsedHandHistory.insert(json)

    x.onComplete {
      x =>
        println("done ! ok? : "+x.isSuccess)
    }
    Await.result(x, 50 seconds)


  }

}
