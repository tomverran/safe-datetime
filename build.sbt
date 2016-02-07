name := "safe-datetime"

version := "0.1"

scalaVersion := "2.11.7"

libraryDependencies ++= Seq("org.specs2" %% "specs2-core" % "3.7" % "test")

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.8.0"

scalacOptions in Test ++= Seq("-Yrangepos")

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

bintrayVcsUrl := Some("https://github.com/tomverran/safe-datetime.git")