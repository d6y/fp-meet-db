package code

import scalaz._, Scalaz._
import scalaz.concurrent.Task
import doobie.imports._

object DoobieExample extends App {

  case class Country(
     code:       String,
     name:       String,
     population: Int,
     gnp:        Float)

  val code: String = "GBR"

  val simple =
    sql"""select code from country where code="GBR"""".query[String]

  val query =
    sql"select code, name, population, gnp from country where code = $code".query[Country]

  val xa = DriverManagerTransactor[Task](
    "org.postgresql.Driver",
    "jdbc:postgresql:world",
    System.getProperty("user.name") , "")

  val uk: Option[Country] = query.option.transact(xa).run

  println(uk)

  // Compose...

  def smallerThan(other: Country) =
    sql"select name from country where population < ${other.population}".query[String]

  val names = for {
    uk    <- query.unique
    small <- smallerThan(uk).list
  } yield small

  println(
    names.transact(xa).attempt.run
  )

}
