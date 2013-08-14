name := "tswrdb-api"

logBuffered := true

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-core" % "2.2.1"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-annotations" % "2.2.1"

libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.2.1"

libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.10" % "2.2.2"

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.0.1" % "test"

libraryDependencies += "junit" % "junit" % "4.11" % "test"
