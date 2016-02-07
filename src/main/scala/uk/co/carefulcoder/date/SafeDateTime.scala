package uk.co.carefulcoder.date
import uk.co.carefulcoder.date.datetimelibrary.DateTimeLibrary

// a safe date time has a timezone, local time and zoned time
case class SafeDateTime[T <: TimeZone, L, Z](local: L)

object SafeDateTime {
  /**
   * Ability go to from a SafeDateTime to a ZonedDateTime
   */
  implicit def toZoned[T <: TimeZone, L, Z](in: SafeDateTime[T, L, Z])(implicit library: DateTimeLibrary[L, Z], tz: T): Z =
    library.localToZoned(in.local, tz)

  /**
   * Change the timezone of the SafeDateTime, like DateTime.withZoneSameLocal
   */
  implicit def moveZone[T <: TimeZone, O <: TimeZone, L, Z](in: SafeDateTime[T, L, Z])(implicit inTz: T, outTz: O, library: DateTimeLibrary[L, Z]): SafeDateTime[O, L, Z] =
    SafeDateTime[O, L, Z](library.zonedToLocal(library.zonedToZoned(library.localToZoned(in.local, inTz), outTz)))

  /**
   * Provide the ability to go from a ZonedDateTime to a SafeDateTime easily
   * The "enforce" method will blow up if the timezone of the ZonedDateTime does not match the desired timezone
   * of the SafeDateTime, to ensure you don't accidentally convert local times when you're not intending to.
   *
   * "become" does silently convert the time into the timezone you've given.
   */
  implicit class ToSafe[Z, L](zonedDateTime: Z)(implicit library: DateTimeLibrary[L, Z]) {
    
    def become[T <: TimeZone](implicit t: T) =
      SafeDateTime[T, L, Z](library.zonedToLocal(library.zonedToZoned(zonedDateTime, t)))
    
    def enforce[T <: TimeZone](implicit t: T) = {
      assert(library.isZone(zonedDateTime, t), "ZONE CHECK FAILED")
      become
    }
  }
}
