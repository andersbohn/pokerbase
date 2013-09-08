package processor.parsers

import scala.util.parsing.combinator._


object Sample extends JavaTokenParsers with App with NonGreedy {

  override val whiteSpace = """[ \t]+""".r

  val CRLF = """\r\n""".r | """\n""".r

  val name = """[a-zA-Z0-9\._-]+""".r

  val actions = name ~ nonGreedy2(name, "dies" | "kills player " ~ name | "sleeps all day")

  val parser = repsep(actions, CRLF)

  val sample =
    """jake dies
      |harry johanson sleeps all day
      |billy kills player jake
    """.stripMargin.trim

  val all = parseAll(parser, sample)
  print(all)
}


