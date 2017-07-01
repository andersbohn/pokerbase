package pokerbase.model

import pokerbase.domain._

case class HandHistory(handId: String, handHistory: String)

case class ParsedHandHistory(handId: String, playerId: String, owner: Option[String], table: Table, header: Header, actions: List[Action])
