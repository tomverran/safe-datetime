package io.tvc.safedatetime.library

import java.time.{ZoneId, ZonedDateTime, LocalDateTime}
import io.tvc.safedatetime.{TimeZone, SafeDateTime}

object JavaTime {
  type DateTime[T <: TimeZone] = SafeDateTime[T, LocalDateTime, ZonedDateTime]
  implicit object Impl extends DateTimeLibrary[LocalDateTime, ZonedDateTime] {
    override def localToZoned(local: LocalDateTime, tz: TimeZone): ZonedDateTime = local.atZone(ZoneId.of(tz.id))
    override def zonedToZoned(zoned: ZonedDateTime, tz: TimeZone): ZonedDateTime = zoned.withZoneSameInstant(ZoneId.of(tz.id))
    override def zonedToLocal(zoned: ZonedDateTime): LocalDateTime = zoned.toLocalDateTime
    override def isZone(z: ZonedDateTime, tz: TimeZone) = z.getZone.getId == tz.id
  }
}
