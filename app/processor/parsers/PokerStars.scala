package processor.parsers

import scala.util.parsing.combinator._

import domain._
import util.Time
import java.util.Date
import processor.parsers
import model.ParsedHandHistory

object PokerStars extends JavaTokenParsers with NonGreedy {


  // CASH Games only, testet only on 6-max
  // Dealt once only!

  override val whiteSpace = """[ \t]+""".r

  val PokerStars = "PokerStars"


  val handHeader: Parser[Any] = """Hand\s+#""".r
  val handNumber: Parser[Hand] = handHeader ~> wholeNumber ^^ Hand

  val CRLF = """\r\n""".r | """\n""".r


  val word = """\w+""".r
  val nonQuote = """[^']+""".r
  val nonDoubleQuote = """[^"]+""".r
  val nonEndParenthesis = """[^\)]*""".r
  val textLiteral = """[a-zA-Z0-9 ,\._-]+""".r
  val timezoneString = """[a-zA-Z]+""".r


  // The presence of any sort of character in playernames requires non-greedy match for the remainder of the line. Note also that eg "<plyname> joins table" can occur at any place in the file
  val playerNameLiteral = """[a-zA-Z0-9;:\(\)=\-!"#\$%&'\*\+,\./<>\?@\[\]\^_´`\{\}~]+""".r

  def nonGreedyPlayerName[T](tailMatch: Parser[T]): Parser[(String, T)] = playerNameLiteral ~ nonGreedy2(playerNameLiteral, tailMatch) ^^ {
    //    (String, List(Any) ~ Any)
    case ply1 ~ (lst ~ any) =>
      (extractPlyName(ply1, lst), any.asInstanceOf[T])
  }


  def extractPlyName[T](ply1: String, lst: List[Any]): String = {
    val name = ply1 + " " + lst.mkString(" ")
    val dropFirstColon = if (name.startsWith(":")) name.tail else name
    val dropLastColon = if (dropFirstColon.endsWith(":")) dropFirstColon.init else dropFirstColon
    dropLastColon.trim
  }

  val HoldemNL: Parser[GameType] = """Hold'em No Limit""".r ^^ (x => NlHoldem)
  val amount: Parser[Double] = "$" ~ floatingPointNumber ^^ (_._2.toDouble)

  val pAmount: Parser[Double] = "(" ~> amount <~ ")"

  val CashStakes: Parser[DualBlind] = "(" ~> amount ~ "/" ~ amount ~ "USD" <~ ")" ^^ {
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
  val doubleQuotedString = "\"" ~> nonDoubleQuote <~ "\""
  val rank = """[1-9TJQKAtjkqa]""".r ^^ (_.head)
  val suit = """[dcsh]""".r ^^ (_.head)
  val card = rank ~ suit ^^ {
    case r ~ s => Card(r, s)
  }

  val holecard: Parser[Card] = "[" ~> card <~ "]"

  val holecards: Parser[HoldemHolecards] = "[" ~> card ~ card <~ "]" ^^ {
    case card1 ~ card2 => HoldemHolecards(card1, card2)
  }

  //  val finalHand = nonEndParenthesis ^^ FinalHand
  val finalHand = """[a-zA-Z0-9 ,\+\._-]+""".r ^^ FinalHand

  //  [Ks Ad] (a pair of Sevens - Ace+King kicker)
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
    case hc ~ and ~ ("won" ~ sAmount) ~ wwith ~ fh => ShowedAndWon(hc, fh, sAmount.toString.toDouble)
  }

  val endStatus: Parser[EndStatus] = esCollected | esMucked | esFolded | esFoldedPF | esShowed

  val optionalSpotInfo: Parser[Option[String]] = opt("(button)" | "(small blind)" | "(big blind)")

  val nameAndEndStatus: parsers.PokerStars.Parser[(String, ~[Option[String], EndStatus])] = nonGreedyPlayerName(optionalSpotInfo ~ endStatus)

  val seatSummary = "Seat" ~> wholeNumber ~ nameAndEndStatus ^^ {
    //    case x=> x._2._
    case ~(seatNumber, (name, ~(optSpot, tEndStatus))) => SeatSummary(seatNumber.toInt, name, optSpot, tEndStatus)

  }
  // TODO Not sure if "and is all-in" occurs on blinds posts...
  val postSmallBlind = "posts big blind " ~> amount ~ opt("and is all-in") ^^ (x => PostsBigBlind(x._1, x._2.isDefined))
  val postBigBlind = "posts small blind " ~> amount ~ opt("and is all-in") ^^ (x => PostsSmallBlind(x._1, x._2.isDefined))
  val postSmallAndBigBlind = "posts small & big blinds " ~> amount ~ opt("and is all-in") ^^ (x => PostsSmallAndBigBlind(x._1, x._2.isDefined))
  val sitsOut = "sits out" ^^ (_ => SitsOut)
  val leavesTheTable = "leaves the table" ^^ (_ => Leaves)

  val preflopAction = nonGreedyPlayerName(postSmallBlind | postBigBlind | postSmallAndBigBlind | sitsOut | leavesTheTable) ^^ {
    case (name, action) => PlayerAction(name, action)
  }


  val statusCollected = "collected" ~> amount <~ "from pot" ^^ CollectedAction

  val chats = "said, " ~> doubleQuotedString ^^ Chats

  val statusWillBeAllowedAfterBtn = "will be allowed to play after the button" ^^ (_ => WillBeAllowedAfterButton)
  val statusIsDisconnected = "is disconnected" ^^ (_ => IsDisconnected)
  val statusTimedOut = "has timed out" ^^ (_ => TimedOut)
  val statusConnected = "is connected" ^^ (_ => IsConnected)
  val wasRemoved = "was removed from the table for failing to post" ^^ (_ => WasRemovedFromTheTableForFailingToPost)
  val timedOutWhileDisconnected = ("has timed out while being disconnected" | "has timed out while disconnected") ^^ (_ => TimedOutWhileDisconnected)
  val joinsTable = "joins the table at seat #" ~> wholeNumber ^^ (x => JoinsTable(x.toInt))

  val statusActions: Parser[Action] = statusCollected | leavesTheTable | statusWillBeAllowedAfterBtn | wasRemoved | timedOutWhileDisconnected | statusIsDisconnected | statusConnected | joinsTable | statusTimedOut | chats


  /*
    val seating = "Seat" ~> wholeNumber ~ nonGreedyPlayerName(amountInChips) ^^ {
      case ~(number, (name, amountDouble: Double)) =>
        Seating(number.toInt, name, amountDouble)
    }
    val seatSummary = "Seat" ~> wholeNumber ~ nameAndEndStatus ^^ {
      //    case x=> x._2._
      case ~(seatNumber, (name, ~(optSpot, tEndStatus))) => SeatSummary(seatNumber.toInt, name, optSpot, tEndStatus)

    }
  */

  val statusAction = nonGreedyPlayerName(statusActions) ^^ {
    case (name, action) => PlayerAction(name, action)
  }

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
  val haFolds = "folds" ~ (opt(holecard) | opt(holecards)) ^^ (_ => Folds) // TODO add flashed cards here

  val haChecks = "checks" ^^ (_ => IsSittingOut)

  val haMucksHand = "mucks hand" ^^ (_ => MucksHand)

  val haDoesntShow = "doesn't show hand" ^^ (_ => DoesntShow)

  val haCalls = "calls " ~> amount ~ opt("and is all-in") ^^ (x => Calls(x._1, x._2.isDefined))

  val haBets = "bets" ~> amount ~ opt("and is all-in") ^^ (x => Bets(x._1, x._2.isDefined))

  val handAction = nonGreedyPlayerName(haSittingOut | haFolds | haChecks | haMucksHand | raises | haDoesntShow | haCalls | haBets | shows) ^^ {
    case (name, action) => PlayerAction(name, action)
  }

  val potInfoUncalled = "Uncalled bet (" ~> amount ~ ") returned to " ~ rep(playerNameLiteral) ^^ {
    case amountDouble ~ sep ~ lst => PotInfoUncalled(amountDouble, lst.mkString(" "))
  }
  val potInfoTotal = "Total pot $" ~> decimalNumber ~ "|" ~ "Rake $" ~ decimalNumber ^^ {
    case amountDouble ~ sep1 ~ sep2 ~ rake => PotInfoTotal(amountDouble.toDouble, rake.toDouble)
  }
  val potInfo = potInfoUncalled | potInfoTotal


  val dealtCard = "Dealt to" ~> nonGreedyPlayerName(holecards) ^^ {
    case (name, hc) => DealtTo(name, hc)
  }

  val flop = "*** FLOP *** [" ~> card ~ card ~ card <~ "]" ^^ (cards => Flop(cards._1._1, cards._1._2, cards._2))
  val turn = "*** TURN *** [" ~ card ~ card ~ card ~ "] [" ~> card <~ "]" ^^ Turn
  val river = "*** RIVER *** [" ~ card ~ card ~ card ~ card ~ "] [" ~> card <~ "]" ^^ River
  val board = "Board [" ~> card ~ card ~ card ~ opt(card) ~ opt(card) <~ "]" ^^ {
    case flop1 ~ flop2 ~ flop3 ~ optTurn ~ optRiver => Board(Flop((flop1, flop2, flop3)), optTurn.map(Turn), optRiver.map(River))
  }

  val infoHolecards = "*** HOLE CARDS ***" ^^ (_ => Holecards)
  val infoShowdown = "*** SHOW DOWN ***" ^^ (_ => Showdown)
  val infoSummary = "*** SUMMARY ***" ^^ (_ => Summary)

  val badParts = "*** FIRST TURN ***" ^^ (_ => NoSuccess)

  val infoSeparators = infoHolecards | infoShowdown | infoSummary

  // TODO would be nice if we could combine more repsep(xx,CRLF), but can't make it work correctly...

  def filterActions(products: List[Product with Serializable]): List[Action] = {
    products.collect {
      case action: Action => action
    }
  }

  val parser = header ~ CRLF ~
    table ~ CRLF ~ repsep(
    //    badParts |
    seating |
      preflopAction |
      infoSeparators |
      dealtCard |
      handAction |
      statusAction |
      potInfo |
      flop | turn | river | board |
      seatSummary, CRLF) ^^ {
    case h ~ lf1 ~ t ~ lf2 ~ actions =>

      val filtered = filterActions(actions)

      ParsedHandHistory(t, h, filtered)
  }

  // TODO some sort of consistency validation would be nice

}


