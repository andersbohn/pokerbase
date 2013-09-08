package processor.parsers

import scala.io.Source
import org.junit.{Test, Ignore}


class NamesProcessor {


  @Test
  @Ignore
  def countCharacter {
    val counts = Source.fromFile("/Users/abj/Projects/pokerbase/tools/odd-player-names.csv") getLines() map (l => l -> l.count(_ == ';'))
    val filtered = counts.filter(_._2 > 1)
    println("->" + filtered.mkString("\n"))
  }

  @Test
  @Ignore
  def listUniqieListOfCharacters {

    val chars = Source.fromFile("/Users/abj/Projects/pokerbase/tools/odd-player-names.csv") getLines() map (l => l.toSet)
    val flat = chars.toSet.flatten.toList.sorted
    println("->" + flat.mkString("\n"))
  }


}
