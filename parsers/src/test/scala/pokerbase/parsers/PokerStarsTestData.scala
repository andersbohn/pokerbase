package processor.parsers

import pokerbase.model.ParsedHandHistory
import pokerbase.parsers.PokerStars
import spray.json._


class PokerStarsTestData {
  val parsers = PokerStars


  def testParseHh1 {
    val all = PokerStars.parseAll(PokerStars.parser, tstHh1)
    all
    println(all)
    println("IN: >" + tstHh1 + "<")
  }


  def testParseHh2 {
    val all = PokerStars.parseAll(PokerStars.parser, tstHh2)
    all
    println(all)

    println("IN: >" + tstHh2 + "<")
  }


  def testParseHh3 {
    val all = PokerStars.parseAll(PokerStars.parser, tstHh3)
    all
    println(all)

    println("IN: >" + tstHh3 + "<")
  }


  import pokerbase.domain.NLHEJsonProtocol._

  implicit val formatParsedHandHistory = jsonFormat6(ParsedHandHistory)

  def testParseHh4 {
    val all = PokerStars.parseAll(PokerStars.parser, tstHh4)
    all
    println(all)

    println("IN: >" + tstHh4 + "<")


    println("JSON > " + all.get.toJson.prettyPrint)
  }


  def testParseTournamentHh1 {
    val all = PokerStars.parseAll(PokerStars.parser, tstTournamentHh1)
    all
    println(all)

    println("IN: >" + tstTournamentHh2 + "<")


    println("JSON > " + all.get.toJson.prettyPrint)
  }


  def testParseTournamentHh2 {
    val all = PokerStars.parseAll(PokerStars.parser, tstTournamentHh2)
    all
    println(all)

    println("IN: >" + tstTournamentHh2 + "<")


    println("JSON > " + all.get.toJson.prettyPrint)
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

  val tstHh3 =
    """PokerStars Hand #103174639058:  Hold'em No Limit ($0.10/$0.25 USD) - 2013/08/24 21:00:31 CET [2013/08/24 15:00:31 ET]
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

  val tstHh4 =
    """
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
      | """.stripMargin.trim

  val tstTournamentHh1 =
    """PokerStars Hand #94697087383: Tournament #695727164, $13.89+$1.11 USD Hold'em No Limit - Level I (10/20) - 2013/02/26 23:49:45 CET [2013/02/26 17:49:45 ET]
      |Table '695727164 2' 9-max Seat #1 is the button
      |Seat 1: tlj85 (1500 in chips)
      |Seat 2: Gajukas (1500 in chips)
      |Seat 3: aazaa (1500 in chips)
      |Seat 4: joapmm (1500 in chips)
      |Seat 5: o'canada1979 (1500 in chips)
      |Seat 6: Rodd_Hammers (1500 in chips)
      |Seat 7: Dexter8010 (1500 in chips)
      |Seat 8: jumaduq (1500 in chips)
      |Seat 9: KINGBAZZ1986 (1500 in chips)
      |Gajukas: posts small blind 10
      |aazaa: posts big blind 20
      |*** HOLE CARDS ***
      |Dealt to aazaa [7d Js]
      |joapmm: folds
      |o'canada1979: calls 20
      |Rodd_Hammers: folds
      |Dexter8010: folds
      |jumaduq: raises 100 to 120
      |KINGBAZZ1986: folds
      |tlj85: folds
      |Gajukas: folds
      |aazaa: folds
      |o'canada1979: calls 100
      |*** FLOP *** [Qh 7s As]
      |o'canada1979: bets 110
      |jumaduq: raises 110 to 220
      |o'canada1979: calls 110
      |*** TURN *** [Qh 7s As] [4d]
      |o'canada1979: checks
      |jumaduq: checks
      |*** RIVER *** [Qh 7s As 4d] [2d]
      |o'canada1979: bets 330
      |jumaduq: calls 330
      |*** SHOW DOWN ***
      |o'canada1979: shows [2s 5s] (a pair of Deuces)
      |jumaduq: shows [Ad Ks] (a pair of Aces)
      |jumaduq collected 1370 from pot
      |*** SUMMARY ***
      |Total pot 1370 | Rake 0
      |Board [Qh 7s As 4d 2d]
      |Seat 1: tlj85 (button) folded before Flop (didn't bet)
      |Seat 2: Gajukas (small blind) folded before Flop
      |Seat 3: aazaa (big blind) folded before Flop
      |Seat 4: joapmm folded before Flop (didn't bet)
      |Seat 5: o'canada1979 showed [2s 5s] and lost with a pair of Deuces
      |Seat 6: Rodd_Hammers folded before Flop (didn't bet)
      |Seat 7: Dexter8010 folded before Flop (didn't bet)
      |Seat 8: jumaduq showed [Ad Ks] and won (1370) with a pair of Aces
      |Seat 9: KINGBAZZ1986 folded before Flop (didn't bet)
      | """.stripMargin.trim


  val tstTournamentHh2 =
    """PokerStars Hand #94697468575: Tournament #695727164, $13.89+$1.11 USD Hold'em No Limit - Level II (15/30) - 2013/02/26 23:57:38 CET [2013/02/26 17:57:38 ET]
      |Table '695727164 2' 9-max Seat #9 is the button
      |Seat 1: tlj85 (1635 in chips)
      |Seat 2: Gajukas (1785 in chips)
      |Seat 3: aazaa (1470 in chips)
      |Seat 4: joapmm (1470 in chips)
      |Seat 5: o'canada1979 (290 in chips)
      |Seat 6: Rodd_Hammers (1470 in chips)
      |Seat 7: Dexter8010 (1330 in chips)
      |Seat 8: jumaduq (2055 in chips)
      |Seat 9: KINGBAZZ1986 (1995 in chips)
      |tlj85: posts small blind 15
      |Gajukas: posts big blind 30
      |*** HOLE CARDS ***
      |Dealt to aazaa [Ad 8c]
      |aazaa: raises 60 to 90
      |joapmm: folds
      |o'canada1979: raises 200 to 290 and is all-in
      |Rodd_Hammers: folds
      |Dexter8010: folds
      |jumaduq: folds
      |KINGBAZZ1986: folds
      |tlj85: folds
      |Gajukas: folds
      |aazaa: calls 200
      |*** FLOP *** [Tc 8h 5d]
      |*** TURN *** [Tc 8h 5d] [5s]
      |*** RIVER *** [Tc 8h 5d 5s] [4c]
      |*** SHOW DOWN ***
      |aazaa: shows [Ad 8c] (two pair, Eights and Fives)
      |o'canada1979: shows [Qd Ks] (a pair of Fives)
      |aazaa collected 625 from pot
      |o'canada1979 finished the tournament in 18th place
      |*** SUMMARY ***
      |Total pot 625 | Rake 0
      |Board [Tc 8h 5d 5s 4c]
      |Seat 1: tlj85 (small blind) folded before Flop
      |Seat 2: Gajukas (big blind) folded before Flop
      |Seat 3: aazaa showed [Ad 8c] and won (625) with two pair, Eights and Fives
      |Seat 4: joapmm folded before Flop (didn't bet)
      |Seat 5: o'canada1979 showed [Qd Ks] and lost with a pair of Fives
      |Seat 6: Rodd_Hammers folded before Flop (didn't bet)
      |Seat 7: Dexter8010 folded before Flop (didn't bet)
      |Seat 8: jumaduq folded before Flop (didn't bet)
      |Seat 9: KINGBAZZ1986 (button) folded before Flop (didn't bet)""".stripMargin.trim
}
