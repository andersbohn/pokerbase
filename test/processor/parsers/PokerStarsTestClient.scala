package processor.parsers

import org.junit.{Ignore, Test}
import java.util.Scanner
import java.io.File
import model.ParsedHandHistory


class PokerStarsTestClient {
  val parsers = PokerStars

  val BOM: Int = 65279


  @Test
  @Ignore
  def testLoadAllInFile {
    val name = "/Users/abj/Projects/pokerbase/test/harvester/files/all-cash-hh.csv"
//        val name = "/Users/abj/Projects/pokerbase/test/harvester/files/HH20130824 Schedios VI - $0.10-$0.25 - USD No Limit Hold'em.txt"
    //    val name = "/Users/abj/Projects/pokerbase/test/harvester/files/HH20130824 Coelestina III - $0.10-$0.25 - USD No Limit Hold'em.txt"

    val delimiter: Scanner = new Scanner(new File(name), "UTF-8").useDelimiter("\\n\\n")
    var cntAll = 0
    var cntErrors = 0
    while (delimiter.hasNext) {
      var s = delimiter.next().trim
      if (!s.isEmpty) {
        if (s.charAt(0) == BOM) {
          s = s.substring(1)
        }
        cntAll += 1
        val all = PokerStars.parseAll(PokerStars.parser, s)
        if (all.isEmpty) {
          println("error -> " + all)
          println("   in ->" + s + "<")
          cntErrors += 1
        }
      }
    }

    println (s"""Summary: $cntErrors of $cntAll """)

  }

  @Test
  @Ignore
  def testParseAndSaveOneFromFile {
    val name = "/Users/abj/Projects/pokerbase/test/harvester/files/HH20130824 Schedios VI - $0.10-$0.25 - USD No Limit Hold'em.txt"

    val delimiter: Scanner = new Scanner(new File(name), "UTF-8").useDelimiter("\\n\\n")

    var s = delimiter.next().trim
    if (s.charAt(0) == BOM) {
      s = s.substring(1)
    }

    val all = PokerStars.parseAll(PokerStars.parser, s)
    if (all.isEmpty) {
      println("error -> " + all)
      println("   in ->" + s + "<")
    } else {
      val parsedHandHistory: ParsedHandHistory = all.get
      ParsedHandHistory.insert(parsedHandHistory)
    }
  }

}
