package processor.parsers

import scala.util.parsing.combinator._

import domain._
import util.Time
import java.util.Date

object PokerStars extends JavaTokenParsers {

  override val whiteSpace = """[ \t]+""".r

  val PokerStars: Parser[Any] = "PokerStars"


  val handHeader: Parser[Any] = """Hand\s+#""".r
  val handNumber: Parser[Any] = handHeader ~> wholeNumber ^^ Hand

  val CRLF = """\r\n""".r | """\n""".r


  val word = """\w+""".r
  val nonQuote = """[^\']+""".r
  val textLiteral = """[a-zA-Z0-9 ,\._-]+""".r

  // FIXME abj seems to be playernames with space in them... which breaks the parsing ... maybe start out by escaping??
  val playerNameLiteralWithSpace = """[a-zA-Z0-9 \._-]+""".r
  val playerNameLiteral = """[a-zA-Z0-9\._-]+""".r

  val HoldemNL: Parser[GameType] = """Hold'em No Limit""".r ^^ (x => NlHoldem)

  val amount: Parser[Double] = "$" ~ floatingPointNumber ^^ (_._2.toDouble)
  val pAmount: Parser[Double] = "(" ~> amount <~ ")"

  val CashStakes: Parser[Any] = "(" ~> amount ~ "/" ~ amount ~ "USD" <~ ")" ^^ {
    case sb ~ sep ~ bb ~ currency => DualBlind(sb, bb, currency)
  }

  val DatePart = wholeNumber ~ "/" ~ wholeNumber ~ "/" ~ wholeNumber ^^ {
    case yy ~ sep1 ~ mm ~ sep2 ~ dd => (yy.toInt, mm.toInt, dd.toInt)
  }

  val TimeStamp: Parser[Date] = DatePart ~ wholeNumber ~ ":" ~ wholeNumber ~ ":" ~ wholeNumber ~ ("CET" | "ET") ^^ {
    case (yy, mm, dd) ~ hh ~ sep1 ~ mi ~ sep2 ~ ss ~ tz => Time.timestampFor(yy, mm, dd, hh.toInt, mi.toInt, ss.toInt, tz)
  }

  val DoubleTimeStamp: Parser[Date] = TimeStamp ~ opt("[" ~> TimeStamp <~ "]") ^^ (_._1)

  val typeHeader = HoldemNL ~ CashStakes ~ "-" ~ DoubleTimeStamp ^^ {
    case gameType ~ stakes ~ sep ~ timestamp => (gameType, stakes, timestamp)
  }

  val quotedString = "'" ~> nonQuote <~ "'"

  val rank = """[1-9TJQKAtjkqa]""".r ^^ (_.head)
  val suit = """[dcsh]""".r ^^ (_.head)
  val card = rank ~ suit ^^ {
    case r ~ s => Card(r, s)
  }
  val holecards = "[" ~> card ~ card <~ "]" ^^ {
    case card1 ~ card2 => HoldemHolecards(card1, card2)
  }

  val finalHand = textLiteral

  val pFinalHand = "(" ~> finalHand <~ ")"

  val header = PokerStars ~ handNumber ~ ":" ~ typeHeader ^^ {
    case ps ~ hand ~ sep ~ th => (ps, hand, th)
  }
  val table = "Table" ~> quotedString ~ "6-max Seat" ~ "#" ~ wholeNumber <~ "is the button"
  val seating = "Seat" ~> wholeNumber ~ ":" ~ playerNameLiteral ~ "(" ~ amount ~ "in chips)"

  val endStatus = "collected" <~ pAmount | "mucked " ~ opt(holecards) | "folded on the " ~ ("Turn" | "Flop" | "River") | "folded before Flop" ~ opt("(didn't bet)") | "showed" ~ holecards ~ "and" ~ ("won" ~ pAmount | "lost") ~ "with" ~ finalHand

  val seatSummary = "Seat" ~> wholeNumber <~ ":" ~> playerNameLiteral ~ opt("(button)" | "(small blind)" | "(big blind)") ~ endStatus

  val postSmallBlind = "posts big blind " ~> amount
  val postBigBlind = "posts small blind " ~> amount
  val preflopAction = playerNameLiteral ~ ":" ~ (postSmallBlind | postBigBlind | "sits out" | "leaves the table") | statusAction
  val raises = "raises " ~> amount <~ "to " ~> amount ~ opt("and is all-in")
  val shows = "shows " ~ holecards ~ pFinalHand

  val statusAction = playerNameLiteral ~ ("joins the table at seat #" ~ wholeNumber | "leaves the table" | "will be allowed to play after the button" | "is disconnected" | "is connected")

  val handAction = playerNameLiteral <~ ":" ~ ("is sitting out" | "folds" | "checks" | "mucks hand" | raises | "doesn't show hand" | "calls " ~ amount | "bets" ~ amount | shows)

  val playerAction = handAction | statusAction

  val potAction = playerNameLiteral ~ "collected" ~ amount <~ "from pot"

  val potInfo = "Uncalled bet (" ~> amount <~ ") returned to " ~> playerNameLiteral | "Total pot $" ~> decimalNumber <~ "|" ~ "Rake $" ~> decimalNumber


  val dealtCard = "Dealt to" ~> playerNameLiteral ~ holecards

  val flop = "*** FLOP *** [" ~ card ~ card ~ card ~ "]"
  val turn = "*** TURN *** [" ~ card ~ card ~ card ~ "] [" ~ card ~ "]"
  val river = "*** RIVER *** [" ~ card ~ card ~ card ~ card ~ "] [" ~ card ~ "]"
  val board = "Board [" ~ card ~ card ~ card ~ opt(card) ~ opt(card) ~ "]"

  // TODO would be nice if we could combine more repsep(xx,CRLF), but can't make it work correctly...

  val parser = header ~ CRLF ~
    table ~ CRLF ~ repsep(
    seating | preflopAction |
      "*** HOLE CARDS ***" |
      dealtCard | playerAction | potAction | potInfo |
      flop | turn | river | board |
      "*** SHOW DOWN ***" |
      "*** SUMMARY ***" |
      seatSummary, CRLF)
}


