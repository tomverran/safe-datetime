package io.tvc.safedatetime.library

import org.joda.time.{DateTime => JodaDateTime, LocalDateTime, DateTimeZone}
import io.tvc.safedatetime.{SafeDateTime, TimeZone}

object JodaTime {
  type DateTime[T <: TimeZone] = SafeDateTime[T, LocalDateTime, JodaDateTime]
  implicit object Impl extends DateTimeLibrary[LocalDateTime, JodaDateTime] {
    override def localToZoned(local: LocalDateTime, tz: TimeZone): JodaDateTime = local.toDateTime(DateTimeZone.forID(tz.id))
    override def zonedToZoned(zoned: JodaDateTime, tz: TimeZone): JodaDateTime = zoned.withZone(DateTimeZone.forID(tz.id))
    override def zonedToLocal(zoned: JodaDateTime): LocalDateTime = zoned.toLocalDateTime
    override def isZone(z: JodaDateTime, tz: TimeZone) = z.getZone.getID == tz.id
  }
}
