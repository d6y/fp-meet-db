package code

import scalaz._, Scalaz._

object MyDbTm extends App {

  sealed trait DbOp[Step]

  case class Put[Step](k: String, v: String, next: Step)
    extends DbOp[Step]

  case class Get[Step](k: String, next: String => Step)
    extends DbOp[Step]

  val prog =
    Get("name", n => Put("greeting", s"Hello $n", () => Unit))
}
