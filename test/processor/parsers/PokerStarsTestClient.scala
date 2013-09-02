package processor.parsers

import org.junit.{Ignore, Test}
import java.util.Scanner
import java.io.File


class PokerStarsTestClient {
  val parsers = PokerStars

  val BOM: Int = 65279


  @Test
  @Ignore
  def testLoadAllInFile {
    val name = "/Users/abj/Projects/pokerbase/test/harvester/files/HH20130824 Coelestina III - $0.10-$0.25 - USD No Limit Hold'em.txt"

    val delimiter: Scanner = new Scanner(new File(name), "UTF-8").useDelimiter("\\n\\n")
    while (delimiter.hasNext) {
      var s = delimiter.next().trim
      if (!s.isEmpty) {
        if (s.charAt(0) == BOM) {
          s = s.substring(1)
        }
        val all: PokerStars.ParseResult[PokerStars.~[Any, Any]] = PokerStars.parseAll(PokerStars.parser, s)
        if (all.isEmpty) {
          println("error -> " + all)
          println("   in ->" + s + "<")
        } else {
          println("hand ok")
        }
      }
    }

  }

}
