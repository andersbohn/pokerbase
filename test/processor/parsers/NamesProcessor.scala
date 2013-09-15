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

  @Test
  @Ignore
  def generateJsonFormats {

    val classes = List("NlHoldem", "GameType", "Stakes", "DualBlind", "InfoMarker", "Holecards ", "Showdown", "Summary", "Street", "Flop", "Turn", "River", "Board", "GameTypeInfo", "Header", "Table", "Seating", "Card", "EndStatus", "Collected", "Mucked", "Folded", "FoldedPf", "ShowedAndLost", "ShowedAndWon", "Folds", "IsSittingOut", "Checks", "MucksHand", "DoesntShow", "PostsSmallAndBigBlind", "PostsBigBlind", "PostsSmallBlind", "SitsOut", "Leaves", "Raises", "Shows", "Calls", "Bets", "PlayerAction", "DealtTo", "CollectedAction", "Chats", "JoinsTable", "WillBeAllowedAfterButton", "IsDisconnected ", "TimedOut", "IsConnected", "PotInfoUncalled", "PotInfoTotal", "Hand", "FinalHand", "HoldemHolecards", "SeatSummary")
    println(classes.map(name => s"implicit val jsonFormat$name = Json.format[$name]").mkString("\n"))
  }




}
