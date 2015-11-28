name := "lista4_scala"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies += "commons-cli" % "commons-cli" % "1.3.1"
libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.0"
libraryDependencies += "com.typesafe.akka" % "akka-stream-experimental_2.11" % "2.0-M1"
libraryDependencies += "org.bouncycastle" % "bcprov-jdk15on" % "1.53"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.5" % "test"
libraryDependencies += "com.google.guava" % "guava" % "19.0-rc2"
libraryDependencies += "com.lambdaworks" %% "jacks" % "2.3.3"
libraryDependencies += "commons-io" % "commons-io" % "2.4"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.4"
libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.3"

assemblyJarName in assembly := "lista4.jar"
mainClass in assembly := Some("pl.andrzejressel.bezpieczenstwo.lista4.Main")