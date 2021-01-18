name := "approx_median"

version := "0.1"

scalaVersion := "2.12.10"

libraryDependencies ++= Seq(
  "io.cucumber" %% "cucumber-scala" % "6.8.1" % Test,
  "io.cucumber" % "cucumber-junit" % "6.8.1" % Test,
  "junit" % "junit" % "4.12" % Test,
  "org.scalatest" %% "scalatest" % "3.2.2" % Test
)
