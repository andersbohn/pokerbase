package processor.parsers

import org.junit.Test
import org.scalatest.junit.AssertionsForJUnit
import model.ParsedHandHistory

class PokerStarsTest extends AssertionsForJUnit {
  val parsers = PokerStars


  @Test
  def testParseHh1 {
    val all = PokerStars.parseAll(PokerStars.parser, tstHh1)
    all
    println(all)
    println("IN: >" + tstHh1 + "<")
  }

  @Test
  def testParseHh2 {
    val all = PokerStars.parseAll(PokerStars.parser, tstHh2)
    all
    println(all)

    println("IN: >" + tstHh2 + "<")
  }

  @Test
  def testParseHh3 {
    val all = PokerStars.parseAll(PokerStars.parser, tstHh3)
    all
    println(all)

    println("IN: >" + tstHh3 + "<")
  }

  @Test
  def testParseHh4 {
    val all = PokerStars.parseAll(PokerStars.parser, tstHh4)
    all
    println(all)

    println("IN: >" + tstHh4 + "<")

    import play.api.libs.json._

    import domain.JsonFormats._
    implicit val formatParsedHandHistory = Json.format[ParsedHandHistory]

    println("JSON > "+ Json.prettyPrint(Json.toJson(all.get)))
  }

  @Test
  def testNameColonOrNot {
    val all1 = PokerStars.parseAll(PokerStars.seating, "Seat 1: condoru 001 ($15.18 in chips)")
    assert(all1.get.playerName === "condoru 001")
    val all2 = PokerStars.parseAll(PokerStars.preflopAction, "condoru 001: posts big blind $0.25")
    assert(all2.get.playerName === "condoru 001")
  }


  val tstHh1 =
    """PokerStars Hand #103174700687:  Hold'em No Limit ($0.10/$0.25 USD) - 2013/08/24 21:02:04 CET [2013/08/24 15:02:04 ET]
      |Table 'Coelestina III' 6-max Seat #5 is the button
      |Seat 1: condoru 001 ($15.18 in chips)
      |Seat 2: Gunnar3000 ($52.07 in chips)
      |Seat 3: mr.HekTo ($28.28 in chips)
      |Seat 4: aazaa ($23.99 in chips)
      |Seat 5: ikounelis ($36.18 in chips)
      |Seat 6: Kitombo ($13 in chips)
      |Kitombo: posts small blind $0.10
      |condoru 001: posts big blind $0.25
      |*** HOLE CARDS ***
      |Dealt to aazaa [Ah As]
      |Gunnar3000: folds
      |mr.HekTo: folds
      |aazaa: raises $0.50 to $0.75
      |ikounelis: calls $0.75
      |Kitombo: folds
      |condoru 001: calls $0.50
      |*** FLOP *** [4s 9h Ts]
      |condoru 001: checks
      |aazaa: bets $1.80
      |ikounelis: folds
      |condoru 001: folds
      |Uncalled bet ($1.80) returned to aazaa
      |aazaa collected $2.24 from pot
      |aazaa: doesn't show hand
      |*** SUMMARY ***
      |Total pot $2.35 | Rake $0.11
      |Board [4s 9h Ts]
      |Seat 1: condoru 001 (big blind) folded on the Flop
      |Seat 2: Gunnar3000 folded before Flop (didn't bet)
      |Seat 3: mr.HekTo folded before Flop (didn't bet)
      |Seat 4: aazaa collected ($2.24)
      |Seat 5: ikounelis (button) folded on the Flop
      |Seat 6: Kitombo (small blind) folded before Flop""".stripMargin.trim

  val tstHh2 =
    """PokerStars Hand #103171898362:  Hold'em No Limit ($0.10/$0.25 USD) - 2013/08/24 19:54:57 CET [2013/08/24 13:54:57 ET]
      |Table 'Schedios VI' 6-max Seat #2 is the button
      |Seat 1: PAPOONGA ($32.54 in chips)
      |Seat 2: aazaa ($24.65 in chips)
      |Seat 3: Flotty30 ($10.77 in chips)
      |Seat 4: Bonehead1977 ($12 in chips)
      |Seat 5: yogiberra14 ($25 in chips)
      |Seat 6: DrInfinity ($44.62 in chips)
      |Flotty30: posts small blind $0.10
      |Bonehead1977: posts big blind $0.25
      |*** HOLE CARDS ***
      |Dealt to aazaa [4c Ts]
      |yogiberra14: folds
      |DrInfinity: folds
      |PAPOONGA: folds
      |aazaa: folds
      |Flotty30: calls $0.15
      |Bonehead1977: raises $0.50 to $0.75
      |Flotty30: calls $0.50
      |*** FLOP *** [Qh 6c 3c]
      |Flotty30: checks
      |Bonehead1977: bets $0.75
      |Flotty30: calls $0.75
      |*** TURN *** [Qh 6c 3c] [Kh]
      |Flotty30: checks
      |Bonehead1977: checks
      |*** RIVER *** [Qh 6c 3c Kh] [3h]
      |Flotty30: checks
      |Bonehead1977: checks
      |*** SHOW DOWN ***
      |Flotty30: shows [Ac 6h] (two pair, Sixes and Threes)
      |Bonehead1977: shows [9h 9d] (two pair, Nines and Threes)
      |Bonehead1977 collected $2.86 from pot
      |*** SUMMARY ***
      |Total pot $3 | Rake $0.14
      |Board [Qh 6c 3c Kh 3h]
      |Seat 1: PAPOONGA folded before Flop (didn't bet)
      |Seat 2: aazaa (button) folded before Flop (didn't bet)
      |Seat 3: Flotty30 (small blind) showed [Ac 6h] and lost with two pair, Sixes and Threes
      |Seat 4: Bonehead1977 (big blind) showed [9h 9d] and won ($2.86) with two pair, Nines and Threes
      |Seat 5: yogiberra14 folded before Flop (didn't bet)
      |Seat 6: DrInfinity folded before Flop (didn't bet)""".stripMargin.trim

  val tstHh3 = """PokerStars Hand #103174639058:  Hold'em No Limit ($0.10/$0.25 USD) - 2013/08/24 21:00:31 CET [2013/08/24 15:00:31 ET]
                 |Table 'Coelestina III' 6-max Seat #5 is the button
                 |Seat 2: Gunnar3000 ($52.17 in chips)
                 |Seat 3: mr.HekTo ($27.85 in chips)
                 |Seat 4: aazaa ($25.59 in chips)
                 |Seat 5: ikounelis ($33.89 in chips)
                 |Gunnar3000: posts small blind $0.10
                 |mr.HekTo: posts big blind $0.25
                 |*** HOLE CARDS ***
                 |Dealt to aazaa [9c Kd]
                 |aazaa: folds
                 |ikounelis: raises $0.50 to $0.75
                 |condoru 001 joins the table at seat #1
                 |Gunnar3000: folds
                 |mr.HekTo: folds
                 |Uncalled bet ($0.50) returned to ikounelis
                 |Kitombo joins the table at seat #6
                 |ikounelis collected $0.60 from pot
                 |ikounelis: doesn't show hand
                 |*** SUMMARY ***
                 |Total pot $0.60 | Rake $0
                 |Seat 2: Gunnar3000 (small blind) folded before Flop
                 |Seat 3: mr.HekTo (big blind) folded before Flop
                 |Seat 4: aazaa folded before Flop (didn't bet)
                 |Seat 5: ikounelis (button) collected ($0.60)""".stripMargin.trim

  val tstHh4 = """
                 |
                 |PokerStars Hand #77751355378:  Hold'em No Limit ($0.50/$1.00 USD) - 2012/03/24 23:00:35 CET [2012/03/24 18:00:35 ET]
                 |Table 'Hunnia III' 6-max Seat #1 is the button
                 |Seat 1: John59D ($45.94 in chips)
                 |Seat 2: sharp ($99 in chips)
                 |Seat 3: scorpchess ($408.71 in chips)
                 |Seat 4: aazaa ($95.50 in chips)
                 |Seat 5: pro100lolka ($39.50 in chips)
                 |Seat 6: erikkke ($264.84 in chips)
                 |sharp: posts small blind $0.50
                 |scorpchess: posts big blind $1
                 |*** HOLE CARDS ***
                 |Dealt to aazaa [9s 2c]
                 |aazaa: folds
                 |pro100lolka: folds
                 |erikkke: folds
                 |John59D: folds
                 |sharp: raises $2 to $3
                 |scorpchess: raises $5 to $8
                 |sharp: raises $13.50 to $21.50
                 |scorpchess: raises $387.21 to $408.71 and is all-in
                 |sharp: calls $77.50 and is all-in
                 |Uncalled bet ($309.71) returned to scorpchess
                 |*** FLOP *** [7c 6h 4c]
                 |*** TURN *** [7c 6h 4c] [3h]
                 |*** RIVER *** [7c 6h 4c 3h] [7h]
                 |*** SHOW DOWN ***
                 |sharp: shows [Qc As] (a pair of Sevens)
                 |scorpchess: shows [Ks Ad] (a pair of Sevens - Ace+King kicker)
                 |scorpchess collected $195.20 from pot
                 |*** SUMMARY ***
                 |Total pot $198 | Rake $2.80
                 |Board [7c 6h 4c 3h 7h]
                 |Seat 1: John59D (button) folded before Flop (didn't bet)
                 |Seat 2: sharp (small blind) showed [Qc As] and lost with a pair of Sevens
                 |Seat 3: scorpchess (big blind) showed [Ks Ad] and won ($195.20) with a pair of Sevens
                 |Seat 4: aazaa folded before Flop (didn't bet)
                 |Seat 5: pro100lolka folded before Flop (didn't bet)
                 |Seat 6: erikkke folded before Flop (didn't bet)
                 |""".stripMargin.trim
}
