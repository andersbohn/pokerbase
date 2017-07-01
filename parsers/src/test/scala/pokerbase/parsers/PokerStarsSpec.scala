package pokerbase.parsers

import org.scalatest.FlatSpec

class PokerStarsSpec extends FlatSpec {

  val parsers = PokerStars

  "names without colon " should " work " in {
    val all1 = PokerStars.parseAll(PokerStars.seating, "Seat 1: condoru 001 ($15.18 in chips)")
    all1.get.playerName === "condoru 001"
  }

  "names with colon " should " work " in {
    val all2 = PokerStars.parseAll(PokerStars.preflopAction, "condoru 001: posts big blind $0.25")
    all2.get.playerName === "condoru 001"
  }

}
