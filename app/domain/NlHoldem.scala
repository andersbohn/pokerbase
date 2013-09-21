package domain

import java.util.Date
import play.api.libs.json._

sealed trait GameType {
  def name: String
}

object GameType {

  implicit val implicitFooWrites = new Format[GameType] {

    def reads(json: JsValue): JsResult[GameType] = json match {
      case JsObject(("name", JsString("NLHoldem")) :: tail) => JsSuccess(NlHoldem)
      case _ => JsError("invalid json " + json)
    }

    def writes(foo: GameType): JsValue = {
      Json.obj(
        "name" -> foo.name
      )
    }
  }


}

case object NlHoldem extends GameType {
  val name = "NLHoldem"
}

sealed trait Stakes

case class DualBlind(sb: Double, bb: Double, currency: String) extends Stakes


sealed trait InfoMarker

case object Holecards extends InfoMarker

case object Showdown extends InfoMarker

case object Summary extends InfoMarker

sealed trait Street

case class Flop(cards: (Card, Card, Card)) extends Street

object Flop {
  implicit val formats = new Format[Flop] {
    def reads(json: JsValue): JsResult[Flop] = json match {
      case JsObject(("card1", JsString(card1)) :: ("card2", JsString(card2)) :: ("card3", JsString(card3)) :: Nil) => JsSuccess(Flop(Card(card1), Card(card2), Card(card3)))
      case _ => JsError("invalid json " + json)
    }

    def writes(foo: Flop): JsValue = JsObject(Seq("card1" -> JsString(foo.cards._1.cardString), "card2" -> JsString(foo.cards._2.cardString), "card3" -> JsString(foo.cards._3.cardString)))

  }
}

case class Turn(card: Card) extends Street

case class River(card: Card) extends Street

case class Board(flop: Flop, turn: Option[Turn], river: Option[River]) extends EndStatus


case class GameTypeInfo(gameType: GameType, stakes: DualBlind, timestamp: Date)

case class Header(ps: String, hand: Hand, th: GameTypeInfo)

case class Table(name: String, description: String, button: Int)

case class Seating(number: Int, playerName: String, initialStack: Double)


case class Card(rank: Char, suit: Char) {
  // TODO add some validation
  def cardString = "" + rank + suit
}

object Card {

  def apply(cardString: String): Card = {
    //FIXME abj add some validation
    Card(cardString(0), cardString(1))
  }

  implicit val implicitFooWrites = new Format[Card] {

    def reads(json: JsValue): JsResult[Card] = json match {
      case JsString(cards) => JsSuccess(Card(cards(0), cards(1)))
      case _ => JsError("invalid json " + json)
    }

    def writes(foo: Card): JsValue = JsString("" + foo.cardString)
  }

}

sealed trait EndStatus {
  def name = getClass.getName
}

object EndStatus {

  implicit val implicitFormat = new Format[EndStatus] {

    // FIXME abj these should parse all endstatues

    def reads(json: JsValue): JsResult[EndStatus] = JsError("")

    def writes(o: EndStatus): JsValue = JsString(o.name)
  }
}

case class Collected(amount: Double) extends EndStatus


case class Mucked(holecards: Option[HoldemHolecards]) extends EndStatus

case class Folded(street: String) extends EndStatus

case class FoldedPf(didntBet: Boolean) extends EndStatus

case class ShowedAndLost(hc: HoldemHolecards, finalHand: FinalHand) extends EndStatus

case class ShowedAndWon(hc: HoldemHolecards, finalHand: FinalHand, amount: Double) extends EndStatus


trait Status {

}

sealed trait Action {

}


object Action {

  implicit val jsonFormatSeatSummary = Json.format[SeatSummary]

  implicit val jsonFormatHand = Json.format[Hand]
  implicit val jsonFormatFinalHand = Json.format[FinalHand]
  implicit val jsonFormatHoldemHolecards = Json.format[HoldemHolecards]
  implicit val jsonFormatShows = Json.format[Shows]


  implicit val jsonFormatCalls = Json.format[Calls]
  implicit val jsonFormatBets = Json.format[Bets]
  implicit val jsonFormatDealtTo = Json.format[DealtTo]

  implicit val jsonFormatCollectedAction = Json.format[CollectedAction]

  implicit val jsonFormatChats = Json.format[Chats]
  implicit val jsonFormatJoinsTable = Json.format[JoinsTable]
  implicit val jsonFormatPotInfoUncalled = Json.format[PotInfoUncalled]
  implicit val jsonFormatPotInfoTotal = Json.format[PotInfoTotal]

  implicit val implicitFormat = new Format[Action] {

    // FIXME abj these should parse all actions


    def reads(json: JsValue): JsResult[Action] = JsError("")

    def writes(action: Action): JsValue = action match {
      case Folds | IsSittingOut | Checks | MucksHand | DoesntShow |
           SitsOut | Leaves | WasRemovedFromTheTableForFailingToPost |
           WillBeAllowedAfterButton | IsDisconnected | TimedOut | TimedOutWhileDisconnected |
           IsConnected => JsString(action.getClass.getSimpleName)
      case Raises(amount: Double, to: Double, allIn: Boolean) => JsObject(Seq("Raises" -> JsNumber(amount), "To" -> JsNumber(amount), "isAllIn" -> JsBoolean(allIn)))
      case paa: PostsAmountAction => JsObject(Seq(action.getClass.getSimpleName -> JsNumber(paa.amount), "isAllIn" -> JsBoolean(paa.isAllIn)))
      case shows@Shows(_, _) => Json.toJson(shows)
      case dealtTo@DealtTo(_, _) => Json.toJson(dealtTo)
      case collectedAction@CollectedAction(_) => Json.toJson(collectedAction)
      case chats@Chats(_) => Json.toJson(chats)
      case joinsTable@JoinsTable(seatNumber: Int) => Json.toJson(joinsTable)
      case potInfoUncalled@PotInfoUncalled(_, _) => Json.toJson(potInfoUncalled)
      case potInfoTotal@PotInfoTotal(amount: Double, rake: Double) => Json.toJson(potInfoTotal)
      case hand@Hand(_) => Json.toJson(hand)
      case finalHand@FinalHand(_) => Json.toJson(finalHand)
      case holdemHolecards@HoldemHolecards(card1: Card, card2: Card) => Json.toJson(holdemHolecards)
      case seatSummary@SeatSummary(_, _, _, _) => Json.toJson(seatSummary)
    }
  }

}

case object Folds extends Action

case object IsSittingOut extends Action

case object Checks extends Action

case object MucksHand extends Action

case object DoesntShow extends Action

case object SitsOut extends Action

case object Leaves extends Action


case object WasRemovedFromTheTableForFailingToPost extends Action with Status

case object WillBeAllowedAfterButton extends Action with Status

case object IsDisconnected extends Action with Status

case object TimedOut extends Action with Status

case object TimedOutWhileDisconnected extends Action with Status

case object IsConnected extends Action with Status


case class Shows(holecards: HoldemHolecards, finalHand: FinalHand) extends Action


trait PostsAmountAction extends Action {
  def amount: Double

  def isAllIn: Boolean
}

case class PostsSmallAndBigBlind(amount: Double, isAllIn: Boolean) extends PostsAmountAction

case class PostsBigBlind(amount: Double, isAllIn: Boolean) extends PostsAmountAction

case class PostsSmallBlind(amount: Double, isAllIn: Boolean) extends PostsAmountAction

case class Raises(amount: Double, to: Double, isAllIn: Boolean) extends PostsAmountAction

case class Calls(amount: Double, isAllIn: Boolean) extends PostsAmountAction

case class Bets(amount: Double, isAllIn: Boolean) extends PostsAmountAction


case class PlayerAction(playerName: String, action: Action)

case class DealtTo(playerName: String, holecards: HoldemHolecards) extends Action with Status

case class CollectedAction(amount: Double) extends Action with Status

case class Chats(message: String) extends Action with Status

case class JoinsTable(seatNumber: Int) extends Action with Status

case class PotInfoUncalled(amount: Double, playerName: String) extends Action

case class PotInfoTotal(amount: Double, rake: Double) extends Action

case class Hand(s: String) extends Action

case class FinalHand(s: String) extends Action

case class HoldemHolecards(card1: Card, card2: Card) extends Action

case class SeatSummary(seatNumber: Int, playerName: String, spot: Option[String], endStatus: EndStatus) extends Action


object JsonFormats {

  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros


  implicit val jsonFormatDualBlind = Json.format[DualBlind]


  implicit val jsonFormatGameTypeInfo = Json.format[GameTypeInfo]
  implicit val jsonFormatTable = Json.format[Table]

  implicit val jsonFormatHeader = Json.format[Header]


  implicit val jsonFormatSeating = Json.format[Seating]



  implicit val jsonFormatTurn = Json.format[Turn]
  implicit val jsonFormatRiver = Json.format[River]
  implicit val jsonFormatBoard = Json.format[Board]

  implicit val jsonFormatCollected = Json.format[Collected]
  implicit val jsonFormatMucked = Json.format[Mucked]
  implicit val jsonFormatFolded = Json.format[Folded]
  implicit val jsonFormatFoldedPf = Json.format[FoldedPf]
  implicit val jsonFormatShowedAndLost = Json.format[ShowedAndLost]
  implicit val jsonFormatShowedAndWon = Json.format[ShowedAndWon]
  implicit val jsonFormatPostsSmallAndBigBlind = Json.format[PostsSmallAndBigBlind]
  implicit val jsonFormatPostsBigBlind = Json.format[PostsBigBlind]
  implicit val jsonFormatPostsSmallBlind = Json.format[PostsSmallBlind]
  implicit val jsonFormatRaises = Json.format[Raises]

  implicit val jsonFormatPlayerAction = Json.format[PlayerAction]

}


