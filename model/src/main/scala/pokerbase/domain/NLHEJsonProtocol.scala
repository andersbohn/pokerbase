package pokerbase.domain

import java.util.Date

import spray.json._

object NLHEJsonProtocol extends DefaultJsonProtocol {

  // FIXME jespa remodel with proper date objects and strings in the json
  implicit object JsonDate extends RootJsonFormat[java.util.Date] {
    override def read(json: JsValue): Date = json match {
      case JsNumber(n) => new java.util.Date(n.longValue())
      case _ => sys.error("wrong date")
    }

    override def write(obj: Date): JsValue = {
      JsNumber(obj.getTime)
    }
  }

  implicit object JsonGameType extends RootJsonFormat[GameType] {

    override def read(json: JsValue): GameType = {
      if (json.asJsObject.getFields("name").headOption.exists(_ == JsString("NLHoldem"))) {
        NlHoldem
      } else {
        sys.error("invalid json " + json)
      }
    }

    override def write(obj: GameType): JsValue = JsObject("name" -> JsString(obj.name))
  }

  implicit val jsonFormatHand = jsonFormat1(Hand)

  implicit val jsonDualBlind = jsonFormat3(DualBlind)
  implicit val jsonTournamentDualBlind = jsonFormat3(TournamentDualBlind)
  implicit val jsonTournamentCost = jsonFormat3(TournamentCost)
  implicit val jsonTournamentInfo = jsonFormat2(TournamentInfo)

  implicit object JsonStakes extends RootJsonFormat[Stakes] {
    def write(c: Stakes) =
      c match {
        case db: DualBlind => db.toJson
        case tc: TournamentDualBlind => tc.toJson
      }

    def read(value: JsValue) = ???
  }

  implicit val jsonFormatGameTypeInfo = jsonFormat4(GameTypeInfo)
  implicit val jsonFormatTable = jsonFormat3(Table)

  implicit val jsonFormatHeader = jsonFormat3(Header)


  implicit val jsonFormatSeating = jsonFormat3(Seating)

  implicit object JsonEndStatus extends RootJsonFormat[EndStatus] {
    override def read(json: JsValue): EndStatus = ???

    override def write(obj: EndStatus): JsValue = ???
  }

  implicit val jsonFormatSeatSummary = jsonFormat4(SeatSummary)

  implicit val jsonFormatPostsSmallAndBigBlind = jsonFormat2(PostsSmallAndBigBlind)
  implicit val jsonFormatPostsBigBlind = jsonFormat2(PostsBigBlind)
  implicit val jsonFormatPostsSmallBlind = jsonFormat2(PostsSmallBlind)
  implicit val jsonFormatRaises = jsonFormat3(Raises)


  implicit object JsonCard extends RootJsonFormat[Card] {

    def read(json: JsValue): Card = json match {
      case JsString(cards) => Card(cards(0), cards(1))
      case _ => sys.error("invalid json " + json)
    }

    def write(foo: Card): JsValue = JsString("" + foo.cardString)
  }

  implicit object JsonFlop extends RootJsonFormat[Flop] {
    override def read(json: JsValue): Flop = json.asJsObject.getFields("card1", "card2", "card3") match {
      case Seq(JsString(card1), JsString(card2), JsString(card3)) => Flop(Card(card1), Card(card2), Card(card3))
      case _ => sys.error("invalid json " + json)
    }

    override def write(foo: Flop): JsValue = {
      JsObject("card1" -> JsString(foo.cards._1.cardString), "card2" -> JsString(foo.cards._2.cardString), "card3" -> JsString(foo.cards._3.cardString))
    }
  }

  implicit val jsonFormatTurn = jsonFormat1(Turn)
  implicit val jsonFormatRiver = jsonFormat1(River)
  implicit val jsonFormatBoard = jsonFormat3(Board)

  implicit val jsonFormatFinalHand = jsonFormat1(FinalHand)
  implicit val jsonFormatHoldemHolecards = jsonFormat2(HoldemHolecards)


  implicit val jsonFormatCollected = jsonFormat1(Collected)
  implicit val jsonFormatMucked = jsonFormat1(Mucked)
  implicit val jsonFormatFolded = jsonFormat1(Folded)
  implicit val jsonFormatFoldedPf = jsonFormat1(FoldedPf)
  implicit val jsonFormatShowedAndLost = jsonFormat2(ShowedAndLost)
  implicit val jsonFormatShowedAndWon = jsonFormat3(ShowedAndWon)

  implicit val jsonFormatShows = jsonFormat2(Shows)


  implicit val jsonFormatCalls = jsonFormat2(Calls)
  implicit val jsonFormatBets = jsonFormat2(Bets)
  implicit val jsonFormatDealtTo = jsonFormat2(DealtTo)

  implicit val jsonFormatCollectedAction = jsonFormat1(CollectedAction)

  implicit val jsonFormatChats = jsonFormat1(Chats)
  implicit val jsonFormatJoinsTable = jsonFormat1(JoinsTable)
  implicit val jsonFormatPotInfoUncalled = jsonFormat2(PotInfoUncalled)
  implicit val jsonFormatPotInfoTotal = jsonFormat2(PotInfoTotal)

  implicit object JsonAction extends RootJsonFormat[Action] {

    // FIXME abj these should parse all actions


    def read(json: JsValue): Action = ???

    def write(action: Action): JsValue =action match {
      case Folds | IsSittingOut | Checks | MucksHand | DoesntShow |
           SitsOut | Leaves | WasRemovedFromTheTableForFailingToPost |
           WillBeAllowedAfterButton | IsDisconnected | TimedOut | TimedOutWhileDisconnected |
           IsConnected => JsString(action.getClass.getSimpleName)
      case Raises(amount: Double, to: Double, allIn: Boolean) => JsObject("Raises" -> JsNumber(amount), "To" -> JsNumber(amount), "isAllIn" -> JsBoolean(allIn))
      case paa: PostsAmountAction => JsObject(action.getClass.getSimpleName -> JsNumber(paa.amount), "isAllIn" -> JsBoolean(paa.isAllIn))
      case shows@Shows(_, _) => shows.toJson
      case dealtTo@DealtTo(_, _) => dealtTo.toJson
      case collectedAction@CollectedAction(_) => collectedAction.toJson
      case chats@Chats(_) => chats.toJson
      case joinsTable@JoinsTable(seatNumber: Int) => joinsTable.toJson
      case potInfoUncalled@PotInfoUncalled(_, _) => potInfoUncalled.toJson
      case potInfoTotal@PotInfoTotal(amount: Double, rake: Double) => potInfoTotal.toJson
      case hand@Hand(_) => hand.toJson
      case finalHand@FinalHand(_) => finalHand.toJson
      case holdemHolecards@HoldemHolecards(card1: Card, card2: Card) => holdemHolecards.toJson
      case collected@Collected(_) => collected.toJson
      case mucked@Mucked(_) => mucked.toJson
      case folded@Folded(_) => folded.toJson
      case foldedPf@FoldedPf(_) => foldedPf.toJson
      case showedAndLost@ShowedAndLost(_, _) => showedAndLost.toJson
      case showedAndWon@ShowedAndWon(_, _, _) => showedAndWon.toJson
      case board@Board(_, _, _) => board.toJson

    }
  }

  implicit val jsonFormatPlayerAction = jsonFormat2(PlayerAction)


}
