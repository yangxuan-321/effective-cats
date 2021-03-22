name := "effective-cats"

version := "0.1"

scalaVersion := "2.13.5"

val catsV         = "2.1.0"

libraryDependencies ++= Seq(
  "org.typelevel"              %% "cats-core"            % catsV,
  "org.typelevel"              %% "cats-free"            % catsV
)