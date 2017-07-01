package pokerbase.parsers

import scala.util.parsing.combinator.Parsers

trait NonGreedy extends Parsers {

  def nonGreedy2[T](rep: => Parser[T], terminal: => Parser[T]) = Parser {
    in =>
      def recurse(in: Input, elems: List[T]): ParseResult[List[T] ~ T] =
        terminal(in) match {
          case Success(x, rest) => Success(new ~(elems.reverse, x), rest)
          case _ =>
            rep(in) match {
              case Success(x, rest) => recurse(rest, x :: elems)
              case ns: NoSuccess => ns
            }
        }

      recurse(in, Nil)
  }
}