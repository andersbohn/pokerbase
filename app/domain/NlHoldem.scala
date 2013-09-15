package domain

import java.util.Date

sealed trait GameType {

}

case object NlHoldem extends GameType

sealed trait Stakes

case class DualBlind(sb: Double, bb: Double, currency: String) extends Stakes

sealed trait InfoMarker

case object Holecards extends InfoMarker

case object Showdown extends InfoMarker

case object Summary extends InfoMarker

sealed trait Street

case class Flop(cards: (Card, Card, Card)) extends Street

case class Turn(card: Card) extends Street

case class River(card: Card) extends Street

case class Board(flop: Flop, turn: Option[Turn], river: Option[River]) extends EndStatus


case class GameTypeInfo(gameType: GameType, stakes: Stakes, timestamp: Date)

case class Header(ps: String, hand: Hand, th: GameTypeInfo)

case class Table(name: String, description: String, button: Int)

case class Seating(number: Int, playerName: String, initialStack: Double)


case class Card(rank: Char, suit: Char) {
  // TODO add some validation
}

sealed trait EndStatus

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

case object Folds extends Action

case object IsSittingOut extends Action

case object Checks extends Action

case object MucksHand extends Action

case object DoesntShow extends Action

case class PostsSmallAndBigBlind(amount: Double) extends Action

case class PostsBigBlind(amount: Double) extends Action

case class PostsSmallBlind(amount: Double) extends Action

case object SitsOut extends Action

case object Leaves extends Action

case class Raises(amount: Double, to: Double, allIn: Boolean) extends Action

case class Shows(holecards: HoldemHolecards, finalHand: FinalHand) extends Action

case class Calls(amount: Double, allIn: Boolean) extends Action

case class Bets(amount: Double, allIn: Boolean) extends Action

case class PlayerAction(playerName: String, action: Action)


case class DealtTo(playerName: String, holecards: HoldemHolecards) extends Action with Status

case class CollectedAction(amount: Double) extends Action with Status

case class Chats(message: String) extends Action with Status

case class JoinsTable(seatNumber: Int) extends Action with Status

case object WasRemovedFromTheTableForFailingToPost extends Action with Status

case object WillBeAllowedAfterButton extends Action with Status

case object IsDisconnected extends Action with Status

case object TimedOut extends Action with Status

case object TimedOutWhileDisconnected extends Action with Status

case object IsConnected extends Action with Status


case class PotInfoUncalled(amount: Double, playerName: String) extends Action

case class PotInfoTotal(amount: Double, rake: Double) extends Action


case class Hand(s: String) extends Action

case class FinalHand(s: String) extends Action

case class HoldemHolecards(card1: Card, card2: Card) extends Action


case class SeatSummary(seatNumber: Int, playerName: String, spot: Option[String], endStatus: EndStatus) extends Action

object JsonFormats {

  import play.api.libs.json.Json

  // Generates Writes and Reads for Feed and User thanks to Json Macros

/*
  implicit val jsonFormatGameTypeInfo = Json.format[GameTypeInfo]
  implicit val jsonFormatDualBlind = Json.format[DualBlind]
  implicit val jsonFormatTable = Json.format[Table]
  implicit val jsonFormatHeader = Json.format[Header]
  implicit val jsonFormatHand = Json.format[Hand]


  implicit val jsonFormatSeating = Json.format[Seating]
  implicit val jsonFormatCard = Json.format[Card]
  implicit val jsonFormatEndStatus = Json.format[EndStatus]
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
  implicit val jsonFormatShows = Json.format[Shows]
  implicit val jsonFormatCalls = Json.format[Calls]
  implicit val jsonFormatBets = Json.format[Bets]
  implicit val jsonFormatPlayerAction = Json.format[PlayerAction]
  implicit val jsonFormatDealtTo = Json.format[DealtTo]
  implicit val jsonFormatCollectedAction = Json.format[CollectedAction]
  implicit val jsonFormatChats = Json.format[Chats]
  implicit val jsonFormatJoinsTable = Json.format[JoinsTable]
  implicit val jsonFormatPotInfoUncalled = Json.format[PotInfoUncalled]
  implicit val jsonFormatPotInfoTotal = Json.format[PotInfoTotal]

  implicit val jsonFormatFinalHand = Json.format[FinalHand]
  implicit val jsonFormatHoldemHolecards = Json.format[HoldemHolecards]
  implicit val jsonFormatSeatSummary = Json.format[SeatSummary]

  implicit val jsonFormatFlop = Json.format[Flop]
  implicit val jsonFormatTurn = Json.format[Turn]
  implicit val jsonFormatRiver = Json.format[River]
  implicit val jsonFormatBoard = Json.format[Board]
*/


}


