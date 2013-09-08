package processor.parsers

import scala.util.parsing.combinator._

import domain._
import util.Time
import java.util.Date

object PokerStars extends JavaTokenParsers with NonGreedy {

  override val whiteSpace = """[ \t]+""".r

  val PokerStars = "PokerStars"


  val handHeader: Parser[Any] = """Hand\s+#""".r
  val handNumber: Parser[Hand] = handHeader ~> wholeNumber ^^ Hand

  val CRLF = """\r\n""".r | """\n""".r


  val word = """\w+""".r
  val nonQuote = """[^']+""".r
  val textLiteral = """[a-zA-Z0-9 ,\._-]+""".r
  val timezoneString = """[a-zA-Z]+""".r


  // The presence of any sort of character in playernames requires non-greedy match for the remainder of the line. Note also that eg "<plyname> joins table" can occur at any place in the file
  val playerNameLiteral = """[a-zA-Z0-9;:\(\)=\-!"#\$%&'\*\+,\./<>\?@\[\]\^_´`\{\}~]+""".r

  def nonGreedyPlayerName[T](tailMatch: Parser[T]) = playerNameLiteral ~ nonGreedy2(playerNameLiteral, tailMatch) ^^ {
    //    (String, List(Any) ~ Any)
    case ply1 ~ (lst ~ any) =>
      (extractPlyName(ply1, lst), any)
  }


  def extractPlyName[T](ply1: String, lst: List[Any]): String = (if (ply1.startsWith(":")) ply1.tail else ply1) + lst.mkString(" ")

  val HoldemNL: Parser[GameType] = """Hold'em No Limit""".r ^^ (x => NlHoldem)
  val amount: Parser[Double] = "$" ~ floatingPointNumber ^^ (_._2.toDouble)

  val pAmount: Parser[Double] = "(" ~> amount <~ ")"

  val CashStakes: Parser[Stakes] = "(" ~> amount ~ "/" ~ amount ~ "USD" <~ ")" ^^ {
    case sb ~ sep ~ bb ~ currency => DualBlind(sb, bb, currency)
  }

  val DatePart = wholeNumber ~ "/" ~ wholeNumber ~ "/" ~ wholeNumber ^^ {
    case yy ~ sep1 ~ mm ~ sep2 ~ dd => (yy.toInt, mm.toInt, dd.toInt)
  }

  val TimeStamp: Parser[Date] = DatePart ~ wholeNumber ~ ":" ~ wholeNumber ~ ":" ~ wholeNumber ~ timezoneString ^^ {
    case (yy, mm, dd) ~ hh ~ sep1 ~ mi ~ sep2 ~ ss ~ tz => Time.timestampFor(yy, mm, dd, hh.toInt, mi.toInt, ss.toInt, tz)
  }

  val DoubleTimeStamp: Parser[Date] = TimeStamp ~ opt("[" ~> TimeStamp <~ "]") ^^ (_._1)

  val typeHeader = HoldemNL ~ CashStakes ~ "-" ~ DoubleTimeStamp ^^ {
    case gameType ~ stakes ~ sep ~ timestamp => GameTypeInfo(gameType, stakes, timestamp)
  }

  val quotedString = "'" ~> nonQuote <~ "'"
  val rank = """[1-9TJQKAtjkqa]""".r ^^ (_.head)
  val suit = """[dcsh]""".r ^^ (_.head)
  val card = rank ~ suit ^^ {
    case r ~ s => Card(r, s)
  }

  val holecards: Parser[HoldemHolecards] = "[" ~> card ~ card <~ "]" ^^ {
    case card1 ~ card2 => HoldemHolecards(card1, card2)
  }

  val finalHand = textLiteral ^^ FinalHand

  val pFinalHand = "(" ~> finalHand <~ ")"
  val header = PokerStars ~ handNumber ~ ":" ~ typeHeader ^^ {
    case ps ~ hand ~ sep ~ th => Header(ps, hand, th)
  }
  val table = "Table" ~> quotedString ~ "6-max Seat" ~ "#" ~ wholeNumber ~ "is the button" ^^ {
    case name ~ typeString ~ hash ~ buttonNumber ~ buttonText => Table(name, typeString, buttonNumber.toInt)
  }

  val amountInChips: Parser[Double] = "(" ~> amount <~ "in chips)"

  val seating = "Seat" ~> wholeNumber ~ nonGreedyPlayerName(amountInChips) ^^ {
    case ~(number, (name, amountDouble: Double)) =>
      Seating(number.toInt, name, amountDouble)
  }

  val esCollected = "collected" ~> pAmount ^^ Collected
  val esMucked = "mucked " ~> opt(holecards) ^^ Mucked
  val esFolded = "folded on the " ~> ("Turn" | "Flop" | "River") ^^ Folded
  val esFoldedPF = "folded before Flop" ~> opt("(didn't bet)") ^^ {
    x => FoldedPf(x.isDefined)
  }
  val esShowed = "showed" ~> holecards ~ "and" ~ ("won" ~ pAmount | "lost") ~ "with" ~ finalHand ^^ {
    case hc ~ and ~ "lost" ~ wwith ~ fh => ShowedAndLost(hc, fh)
    case x =>
      println("showed and won... " + x)
      ShowedAndWon(x._1._1._1._1, x._2, 0.0)
    // FIXME why won't this work?
    //    case hc ~ and ~ "won" ~ amountDouble ~ wwith ~ fh => ShowedAndWon(hc, fh, amountDouble)
  }

  val endStatus = esCollected | esMucked | esFolded | esFoldedPF | esShowed

  val seatSummary = "Seat" ~> wholeNumber ~ nonGreedyPlayerName(opt("(button)" | "(small blind)" | "(big blind)") ~ endStatus)
  val postSmallBlind = "posts big blind " ~> amount ^^ PostsBigBlind
  val postBigBlind = "posts small blind " ~> amount ^^ PostsSmallBlind
  val preflopAction = nonGreedyPlayerName(postSmallBlind | postBigBlind | "sits out" | "leaves the table")

  val statusActions = "collected" ~ amount ~ "from pot" | "leaves the table" | "will be allowed to play after the button" | "is disconnected" | "is connected" | "joins the table at seat #" ~ wholeNumber
  val statusAction = nonGreedyPlayerName(statusActions)
  val loggedStatusAction = log(statusAction)("naam")

  val raises = "raises " ~> amount ~ "to " ~ amount ~ opt("and is all-in") ^^ {
    case amountRaise ~ to ~ amountTo ~ allInOpt =>
      val defined = allInOpt.isDefined
      Raises(amountRaise, amountTo, defined)
  }
  val shows = "shows " ~> holecards ~ pFinalHand ^^ {
    case hc ~ fh => Shows(hc, fh)
  }
  val haSittingOut = "is sitting out" ^^ {
    x => IsSittingOut
  }
  val haFolds = "folds" ^^ {
    x => IsSittingOut
  }
  val haChecks = "checks" ^^ {
    x => IsSittingOut
  }
  val haMucksHand = "mucks hand" ^^ {
    x => MucksHand
  }
  val haDoesntShow = "doesn't show hand" ^^ {
    x => DoesntShow
  }

  val haCalls = "calls " ~> amount ^^ Calls
  val haBets = "bets" ~> amount ^^ Bets

  val handAction = nonGreedyPlayerName(haSittingOut | haFolds | haChecks | haMucksHand | raises | haDoesntShow | haCalls | haBets | shows)

  val playerAction = handAction | statusAction

  val potInfoUncalled = "Uncalled bet (" ~> amount ~ ") returned to " ~ rep(playerNameLiteral) ^^ {
    case amountDouble ~ sep ~ lst => PotInfoUncalled(amountDouble, lst.mkString(" "))
  }
  val potInfoTotal = "Total pot $" ~> decimalNumber ~ "|" ~ "Rake $" ~ decimalNumber ^^ {
    case amountDouble ~ sep1 ~ sep2 ~ rake => PotInfoTotal(amountDouble.toDouble, rake.toDouble)
  }
  val potInfo = potInfoUncalled | potInfoTotal


  val dealtCard = "Dealt to" ~> nonGreedyPlayerName(holecards)

  val flop = "*** FLOP *** [" ~ card ~ card ~ card ~ "]"
  val turn = "*** TURN *** [" ~ card ~ card ~ card ~ "] [" ~ card ~ "]"
  val river = "*** RIVER *** [" ~ card ~ card ~ card ~ card ~ "] [" ~ card ~ "]"
  val board = "Board [" ~ card ~ card ~ card ~ opt(card) ~ opt(card) ~ "]"

  // TODO would be nice if we could combine more repsep(xx,CRLF), but can't make it work correctly...

  val parser = header ~ CRLF ~
    table ~ CRLF ~ repsep(
    seating | preflopAction |
      "*** HOLE CARDS ***" |
      dealtCard | playerAction | potInfo |
      flop | turn | river | board |
      "*** SHOW DOWN ***" |
      "*** SUMMARY ***" |
      seatSummary, CRLF) ^^ {
    case h ~ _ ~ t =>
      (h, t)
  }
}


