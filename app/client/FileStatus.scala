package client

import java.util.Date

sealed trait FileStatus {

  def loaded: Boolean

  def valid: Boolean

  def timestamp: Date

  def lines: Option[Int] = None

}

case class Invalid(timestamp: Date) extends FileStatus {
  def loaded = true

  def valid = false
}

case class Failed(timestamp: Date) extends FileStatus {
  def loaded = false

  def valid = false
}

case class Ok(timestamp: Date, linesRead: Int) extends FileStatus {
  def loaded = true

  def valid = true

  override def lines: Option[Int] = Some(linesRead)
}
