# safe-datetime

The aim of this library is to create a DateTime which is parameterised on its timezone for type safety.
i.e. if you have a method requiring `DateTime[Europe.London]` you can't accidentally provide a `DateTime[America.LosAngeles]` without conversion taking place.

This liberates you from having to repeatedly do things like:
```scala
DateTime.withZoneSameInstant(ZoneId.for("Europe/London"))
```

## Usage

This project is currently hosted on bintray.
Add the following to your build.sbt to get started:

```
resolvers += Resolver.bintrayRepo("javaguychronox", "maven")
libraryDependencies += "io.tvc" %% "safe-datetime" % "0.1"
```

#### Imports

This library works with both Joda and Java time libraries through the `DateTimeLibrary` typeclass.
To use this library you need to import the following:

```scala
import io.tvc.date.SafeDateTime._

//then one of
import io.tvc.date.datetimelibrary.JavaTime._ //if you want to use the Java 8 time library
import io.tvc.date.datetimelibrary.JodaTime._ //if you want to use Joda Time

//then whatever timezones you plan to use
import io.tvc.date.TimeZone.{Europe, America}
```

You will then have a `DateTime[T <: TimeZone]` type alias available which resolves to a `SafeDateTime` object.

#### Wrapping a DateTime/ZonedDateTime into a SafeDateTime

To go from a Java `ZonedDateTime` / Joda `DateTime` object to a SafeDateTime[TimeZone] you use
```scala
.become[A <: TimeZone]
```
which can be called on those objects through an implicit class.

e.g.

```scala
import io.tvc.date.SafeDateTime._
import io.tvc.date.library.JavaTime._
import io.tvc.date.TimeZone.Europe
import java.time.{ZoneId, ZonedDateTime}

val now = ZonedDateTime.now(ZoneId.of(Europe.London.id))
val safe: DateTime[Europe.London] = now.become[Europe.London]
```

will give you the following output on the REPL:

```
now: java.time.ZonedDateTime = 2016-02-07T15:29:02.108Z[Europe/London]
safe: io.tvc.date.library.JavaTime.DateTime[io.tvc.date.TimeZone.Europe.London] = SafeDateTime(2016-02-07T15:29:02.108)
```

A local datetime has been extracted from the `ZonedDateTime` and wrapped in a `SafeDateTime[Europe.London]`.

#### Depending on Time Zones

Once you've wrapped the underlying library DateTime objects into a SafeDateTime you can write functions
that require a particular timezone like so:

```scala
def londonTime(time: DateTime[Europe.London]) = "I say, it is " + time.getHour + " o clock"
def parisTime(time: DateTime[Europe.Paris]) = "mon dieu, c'est " + time.getHour + " heures" //pardon my french
```

Note that there's an implicit conversion between a `DateTime[TimeZone]` and the underlying Joda `DateTime` / Java `ZonedDateTime` object
so all the methods you might call on them (like `getHour()`) are available to you.

If you pass in a `SafeDateTime` with a different timezone it will be automatically converted to the right timezone:


```scala
import io.tvc.date.SafeDateTime._
import io.tvc.date.library.JavaTime._
import io.tvc.date.TimeZone.Europe
import java.time.{ZoneId, ZonedDateTime}

val now = ZonedDateTime.now(ZoneId.of(Europe.London.id))
val safe: DateTime[Europe.London] = now.become[Europe.London]

def londonTime(time: DateTime[Europe.London]) = "I say, it is " + time.getHour + " o clock"
def parisTime(time: DateTime[Europe.Paris]) = "mon dieu, c'est " + time.getHour + " heures"

londonTime(safe)
parisTime(safe)
```

will result something along the lines of

```
res0: String = I say, it is 15 o clock
res1: String = mon dieu, c'est 16 heures
```
