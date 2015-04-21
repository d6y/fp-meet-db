package code

import scalaz._, Scalaz._

object InterpreterExample extends App {

  // A very simple query language and can just query
  // for columns larger than or smaller than some value.
  sealed trait Query[A]
  case class LargerThan[A] (column: String, value: A) extends Query[A]
  case class SmallerThan[A](column: String, value: A) extends Query[A]

  // Free will require us to be able to provide a map function:
  implicit val queryFunctor: Functor[Query] =
    new Functor[Query] {
      def map[A,B](q: Query[A])(f: A => B): Query[B] =
        q match {
          case LargerThan(c, v) => LargerThan(c, f(v))
          case SmallerThan(c,v) => SmallerThan(c, f(v))
        }
    }

  //...which we can use, if we wanted to
  val qPerMill: Query[Int] = LargerThan("population", 10)
  val q = qPerMill.map(_ * 1000)

  // How to evaluate one single query
  // This is fake. I'm not really doing the query,
  // just returning the value you gave

  object QueryEval extends (Query ~> Id.Id){
    def apply[A](q: Query[A]): A =
      q match {
        case LargerThan(c, v)  => v.point[Id]
        case SmallerThan(c, v) => v.point[Id]
      }
  }

  implicit def autoLift[A](q: Query[A]): Free[Query, A] =
    Free.liftF(q)

  // The ratio of the populations above 100 ppl
  // those smaller than 50 less than the largest
  val prog: Free[Query, Float] =
    for {
      big   <- LargerThan("population", 100)
      small <- SmallerThan("population", big-50)
    } yield small / ( big + 0f)

  val prog2 =
    LargerThan("population", 100).flatMap(big => SmallerThan("pop", big-50).map(_ / big))

  val result = prog.foldMap(QueryEval)
  println(result)

  /*
  val progVerboser: Free[Query, Float] =
    for {
      b <- Free.liftF(LargerThan("population", 100): Query[Int])
      s <- Free.liftF(SmallerThan("population", b-50): Query[Int])
    } yield s / ( b + 0f)
   */
}
