package code

import scalaz._, Scalaz._

object MyDbTm extends App {

  sealed trait DbOp[Step]

  case class Put[Step](k: String, v: String, next: () => DbOp[Step])
    extends DbOp[Step]

  case class Get[Step](k: String, next: String => DbOp[Step])
    extends DbOp[Step]

  case class Shutdown() extends DbOp[Unit]

  val prog =
    Get("name",
      n => Put("greeting", s"Hello $n",
        () => Get("greeting", s => { println(s); Shutdown() } ) ) )

  def run[T](prog: DbOp[T], db: Map[String,String]): Unit =
    prog match {
      case Shutdown()   =>
      case Get(k, f)    => run( f(db(k)) , db           )
      case Put(k, v, f) => run( f()      , db + (k -> v))
  }

  run(prog, Map.empty + ("name" -> "Richard"))

  // For the free monad version
  // https://github.com/kenbot/free/blob/master/src/main/scala/kenbot/free/KVS.scala

}