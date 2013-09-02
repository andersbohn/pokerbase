package util

import java.util.{TimeZone, Calendar}

object Time {
  def timestampFor(year: Int, month: Int, day: Int, hour: Int, minute: Int, seconds: Int, timezone: String) = {
    val instance: Calendar = Calendar.getInstance()
    instance.setTimeZone(TimeZone.getTimeZone(timezone))
    instance.set(year, month, day, hour, minute, seconds)
    instance.getTime

  }
}
