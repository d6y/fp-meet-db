name := "fp-meet-db"

scalaVersion := "2.11.6"

resolvers ++= Seq(
    "tpolecat" at "http://dl.bintray.com/tpolecat/maven",
      "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"
    )

libraryDependencies ++= doobie ++ squeryl

lazy val doobieVersion = "0.2.1"

lazy val doobie = Seq(
  "org.tpolecat" %% "doobie-core"               % doobieVersion,
  "org.tpolecat" %% "doobie-contrib-postgresql" % doobieVersion)

lazy val squeryl = Seq(
  "org.squeryl"  %% "squeryl" % "0.9.6-RC3"
)

triggeredMessage in ThisBuild := Watched.clearWhenTriggered

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked", // Enable additional warnings where generated code depends on assumptions
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-Xlint", // Enable recommended additional warnings.
  "-Xfatal-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture"
  )

  triggeredMessage in ThisBuild := Watched.clearWhenTriggered