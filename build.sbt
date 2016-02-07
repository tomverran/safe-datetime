name := "io.tvc.safedatetime.SafeDateTime"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.7" % "test")

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.8.0"

scalacOptions in Test ++= Seq("-Yrangepos")