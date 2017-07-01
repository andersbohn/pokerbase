package processor.parsers

import java.io.File
import java.util.Scanner

import pokerbase.model.ParsedHandHistory
import pokerbase.parsers.PokerStars


object PokerStarsTestClient extends App {
  val parsers = PokerStars

  val BOM: Int = 65279

  val name = "/Users/andersbohn/Projects/pokerbase/parsers/src/test/resources/harvester/files/HH20130824 Schedios VI - $0.10-$0.25 - USD No Limit Hold'em.txt"

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
    //      ParsedHandHistory.insert(parsedHandHistory)
    println(" ///> "+parsedHandHistory)
  }
}
