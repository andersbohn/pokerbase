package domain

import java.util.Date

sealed trait GameType {

}

case object NlHoldem extends GameType

sealed trait Stakes

case class DualBlind(sb: Double, bb: Double, currency: String) extends Stakes

sealed trait Street

case class Flop(cards: Tuple3[Card, Card, Card]) extends Street

case class Turn(card: Card) extends Street

case class River(card: Card) extends Street


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


sealed trait Action {

}

case object Folds extends Action

case object IsSittingOut extends Action

case object Checks extends Action

case object MucksHand extends Action

case object DoesntShow extends Action


case class PostsBigBlind(amount: Double) extends Action

case class PostsSmallBlind(amount: Double) extends Action

case class Raises(amount: Double, to: Double, allIn: Boolean) extends Action

case class Shows(holecards: HoldemHolecards, finalHand: FinalHand) extends Action

case class Calls(amount: Double) extends Action

case class Bets(amount: Double) extends Action


case class PotInfoUncalled(amount: Double, playerName: String)

case class PotInfoTotal(amount: Double, rake: Double)

case class Ident(s: String)

case class Number(n: Int)

case class Hand(s: String)

case class FinalHand(s: String)

case class HoldemHolecards(card1: Card, card2: Card)

case class Str(s: String)

case class AnyStr(s: String)