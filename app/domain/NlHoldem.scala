package domain

sealed trait GameType {

}

case object NlHoldem extends GameType

sealed trait Stakes

case class DualBlind(sb: Double, bb: Double, currency: String) extends Stakes

sealed trait Street

case class Flop(cards: Tuple3[Card, Card, Card]) extends Street

case class Turn(card: Card) extends Street

case class River(card: Card) extends Street

case class Header()

case class Card(rank:Char, suit:Char) {
  // TODO add some validation
}

case class Ident(s: String)

case class Number(n: Int)

case class Hand(s: String)

case class HoldemHolecards(card1:Card, card2: Card)

case class Str(s: String)

case class AnyStr(s: String)