package code

import org.squeryl._
import org.squeryl.dsl._

object SquerylEntrypoint extends PrimitiveTypeMode

import SquerylEntrypoint._

object Earth extends Schema {

  case class Country(
     code:       String,
     name:       String,
     population: Int,
     gnp:        Float)

  val countries: Table[Country] = table[Country]("country")

  on(countries) { c =>
    declare(c.code is (primaryKey))
  }
}

object SquerylExample extends App {

  import Earth._

  DB.init()

  inTransaction {
    Session.currentSession.setLogger(println)

    val query: Query[Country] =
      from(countries) {
        c => where(c.code === "GBR") select(c)
    }

    val uk: Option[Country] = query.headOption
    println(uk)
  }
}

object DB {
  def init(): Unit = {
    import org.squeryl.SessionFactory
    import org.squeryl.Session
    import org.squeryl.adapters.PostgreSqlAdapter

    Class.forName("org.postgresql.Driver")
    SessionFactory.concreteFactory = Some(()=>
      Session.create(
        java.sql.DriverManager.getConnection("jdbc:postgresql:world", System.getProperty("user.name"), ""),
        new PostgreSqlAdapter))
  }
}
