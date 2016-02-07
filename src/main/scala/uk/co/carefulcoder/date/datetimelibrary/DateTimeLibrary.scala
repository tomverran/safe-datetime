package uk.co.carefulcoder.date.datetimelibrary
import uk.co.carefulcoder.date.TimeZone

trait DateTimeLibrary[L, Z] {
  def isZone(zoned: Z, tz: TimeZone): Boolean
  def localToZoned(local: L, tz: TimeZone): Z
  def zonedToZoned(zoned: Z, tz: TimeZone): Z //converting timezone
  def zonedToLocal(zoned: Z): L
}
