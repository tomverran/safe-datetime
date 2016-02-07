import java.time.{ZoneId, ZonedDateTime}
import org.specs2.mutable.Specification
import io.tvc.safedatetime.SafeDateTime._
import io.tvc.safedatetime.library.JavaTime._
import io.tvc.safedatetime.TimeZone.{America, Europe}
import scala.language.implicitConversions
import scala.util.Try

class SafeDateTimeTest extends Specification {

  val parisTime = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of(Europe.Paris.id))
  val londonTime = parisTime.become[Europe.London]

  "SafeDateTime to ZonedDateTime" should {
    "be implicitly converted into a DateTime of the right zone" in {
      parisTime.toLocalTime.getHour mustEqual londonTime.toLocalTime.plusHours(1).getHour
      londonTime.getZone.getId mustEqual Europe.London.id

    }
    "Allow conversion between timezones" in {
      val laTime: DateTime[America.LosAngeles] = londonTime
      laTime.getZone.getId mustEqual America.LosAngeles.id
    }
  }

  "ZonedDateTime to SafeDateTime" should {
    "blow up when attempting to enforce a bad timezone" in {
      val la = ZonedDateTime.now().withZoneSameLocal(ZoneId.of(America.LosAngeles.id))
      Try(la.enforce[Europe.London]).isFailure mustEqual true
    }
    "Convert timezones when become is used" in {
      val rainyRiver = ZonedDateTime.now().withZoneSameLocal(ZoneId.of(America.RainyRiver.id))
      rainyRiver.become[Europe.London].getZone.getId mustEqual Europe.London.id
    }
  }

}
