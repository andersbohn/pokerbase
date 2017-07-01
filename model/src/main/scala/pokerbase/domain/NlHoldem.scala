package pokerbase.domain

import java.util.Date

sealed trait GameType {
  def name: String
}

case object NlHoldem extends GameType {
  val name = "NLHoldem"
}

case class TournamentCost(cost:Double,rake:Double, currency: String)

case class TournamentInfo(tournamentId: String, tournamentCost: TournamentCost)

sealed trait Stakes

case class DualBlind(sb: Double, bb: Double, currency: String) extends Stakes

case class TournamentDualBlind(sb: Int, bb: Int, levelName: String) extends Stakes


sealed trait InfoMarker

case object Holecards extends InfoMarker

case object Showdown extends InfoMarker

case object Summary extends InfoMarker

sealed trait Street

case class Flop(cards: (Card, Card, Card)) extends Street

case class Turn(card: Card) extends Street

case class River(card: Card) extends Street

case class Board(flop: Flop, turn: Option[Turn], river: Option[River]) extends EndStatus


case class GameTypeInfo(gameType: GameType, tourmentInfo:Option[TournamentInfo], stakes: Stakes, timestamp: Date)

case class Header(pokerstarsString: String, hand: Hand, th: GameTypeInfo)

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

}


trait Status {

}


sealed trait Action {

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

case class Hand(handId: String) extends Action

case class FinalHand(handString: String) extends Action

case class HoldemHolecards(card1: Card, card2: Card) extends Action

case class Finished(place:String) extends Action with Status


sealed trait EndStatus extends Action {
  def name = getClass.getName
}

case class Collected(amount: Double) extends EndStatus

case class Mucked(holecards: Option[HoldemHolecards]) extends EndStatus

case class Folded(street: String) extends EndStatus

case class FoldedPf(didntBet: Boolean) extends EndStatus

case class ShowedAndLost(hc: HoldemHolecards, finalHand: FinalHand) extends EndStatus

case class ShowedAndWon(hc: HoldemHolecards, finalHand: FinalHand, amount: Double) extends EndStatus

case class SeatSummary(seatNumber: Int, playerName: String, spot: Option[String], endStatus: EndStatus)


