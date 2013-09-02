package processor.parsers

import org.junit.Test


class PokerStarsTest {
  val parsers = PokerStars


  @Test
  def testParseHh1 {
    val all: PokerStars.ParseResult[PokerStars.~[Any, Any]] = PokerStars.parseAll(PokerStars.parser, tstHh1)
    all
    println(all)
    println("IN: >" + tstHh1 + "<")
  }

  @Test
  def testParseHh2 {
    val all: PokerStars.ParseResult[PokerStars.~[Any, Any]] = PokerStars.parseAll(PokerStars.parser, tstHh2)
    all
    println(all)

    println("IN: >" + tstHh2 + "<")
  }



  val tstHh1 =
    """PokerStars Hand #103173343806:  Hold'em No Limit ($0.10/$0.25 USD) - 2013/08/24 20:29:30 CET [2013/08/24 14:29:30 ET]
      |Table 'Coelestina III' 6-max Seat #2 is the button
      |Seat 1: seiteen ($50.75 in chips)
      |Seat 2: Gunnar3000 ($49.47 in chips)
      |Seat 3: mr.HekTo ($25 in chips)
      |Seat 4: aazaa ($25 in chips)
      |Seat 5: ikounelis ($25.06 in chips)
      |Seat 6: shootsu ($27.44 in chips)
      |mr.HekTo: posts small blind $0.10
      |aazaa: posts big blind $0.25
      |*** HOLE CARDS ***
      |Dealt to aazaa [9s 3d]
      |ikounelis: folds
      |shootsu: folds
      |seiteen: raises $0.50 to $0.75
      |Gunnar3000: folds
      |mr.HekTo: folds
      |aazaa: folds
      |Uncalled bet ($0.50) returned to seiteen
      |seiteen collected $0.60 from pot
      |seiteen: doesn't show hand
      |*** SUMMARY ***
      |Total pot $0.60 | Rake $0
      |Seat 1: seiteen collected ($0.60)
      |Seat 2: Gunnar3000 (button) folded before Flop (didn't bet)
      |Seat 3: mr.HekTo (small blind) folded before Flop
      |Seat 4: aazaa (big blind) folded before Flop
      |Seat 5: ikounelis folded before Flop (didn't bet)
      |Seat 6: shootsu folded before Flop (didn't bet)
      | """.stripMargin.trim
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
}
