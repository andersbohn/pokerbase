package processor.parsers

import scala.util.parsing.combinator._

case class Header()

case class NewLine()

case class Skip()

case class Ident(s: String)

case class Number(n: Int)

case class Hand(s: String)

case class Str(s: String)

case class AnyStr(s: String)

object PokerStars extends JavaTokenParsers {

  override val whiteSpace = """[ \t]+""".r

  //  override val skipWhitespace = false


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
    | """

  val PokerStars: Parser[Any] = "PokerStars"


  val HAND: Parser[Any] = """Hand\s+#""".r
  val handNumber: Parser[Any] = HAND ~> wholeNumber ^^ {
    x => Hand(x)
  }

  val CRLF = """\r\n""".r | """\n""".r
  val myStringLiteral = """\w+""".r
  val amount: Parser[Any] = "$" ~ floatingPointNumber
  val pAmount: Parser[Any] = "(" ~> amount <~ ")"
  //  Hold'em No Limit ($0.10/$0.25 USD) - 2013/08/24 20:29:30 CET [2013/08/24 14:29:30 ET]

  val playerNameLiteral = """[a-zA-Z0-9\._-]+""".r
  val HoldemNL: Parser[Any] = """Hold'em No Limit""".r

  val CashStakes: Parser[Any] = "(" ~> amount <~ "/" ~ amount ~ "USD" <~ ")"
  val DatePart = wholeNumber ~ "/" ~ wholeNumber ~ "/" ~ wholeNumber
  val TimeStamp: Parser[Any] = DatePart ~ wholeNumber ~ ":" ~ wholeNumber ~ ":" ~ wholeNumber ~ ("CET" | "ET")
  val DoubleTimeStamp: Parser[Any] = TimeStamp ~ opt("[" ~> TimeStamp <~ "]")
  val typeHeader = HoldemNL ~ CashStakes <~ "-" ~> DoubleTimeStamp

  val quotedString = "'" ~> rep(myStringLiteral) <~ "'"

  val header = PokerStars ~ handNumber ~ ":" ~ typeHeader
  val table = "Table" ~> quotedString ~ "6-max Seat" ~ "#" ~ wholeNumber <~ "is the button"
  val seating = "Seat" ~> wholeNumber ~ ":" ~ playerNameLiteral ~ "(" ~ amount ~ "in chips)"

  val endStatus = "collected" <~ pAmount | "folded before Flop" ~ opt("(didn't bet)")
  val seatSummary = "Seat" ~> wholeNumber <~ ":" ~> playerNameLiteral ~ opt("(button)" | "(small blind)" | "(big blind)") ~ endStatus

  val postSmallBlind = "posts big blind " ~> amount
  val postBigBlind = "posts small blind " ~> amount
  val preflopAction = playerNameLiteral <~ ":" ~ (postSmallBlind | postBigBlind)
  val raises = "raises " ~> amount <~ "to " ~> amount

  val playerAction = playerNameLiteral <~ ":" ~ ("folds" | raises | "doesn't show hand")

  val potAction = playerNameLiteral ~ "collected" <~ "" ~> amount <~ "from pot"

  val potInfo = "Uncalled bet (" ~> amount <~ ") returned to " ~> playerNameLiteral | "Total pot $" ~> decimalNumber <~ "|" ~ "Rake $" ~> decimalNumber

  val hand = "[" ~> myStringLiteral ~ myStringLiteral <~ "]"

  val dealtCard = "Dealt to" ~> playerNameLiteral ~ hand


  // TODO would be nice if we could combine more repsep(xx,CRLF), but can't make it work correctly...
  val parser = header ~ CRLF ~
    table ~ CRLF ~ repsep(
    seating | preflopAction |
      "*** HOLE CARDS ***" |
      dealtCard | playerAction | potAction | potInfo |
      "*** SUMMARY ***" |
      seatSummary, CRLF)
}


