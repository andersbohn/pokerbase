package pokerbase.base

import org.slf4s.Logging
import pokerbase.model.HandHistory

import scala.concurrent.Future

object HandHistoryController extends Logging {
  def storeHandHistory(idHand: Long, handHistory: HandHistory): Future[Unit] = {
    log.info(s"Storing hand history for id $idHand - ${handHistory.handId}")
    HandHistoryRepository.storeHandHistory(idHand, handHistory: HandHistory)
  }
}
