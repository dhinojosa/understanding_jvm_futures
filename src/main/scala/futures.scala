
/**
  * Demo 10: Scala Futures
  */
import scala.concurrent._
import ExecutionContext.Implicits.global

val future = Future { x + 1 }

future.foreach(x -> println(x))

/**
  * Demo 11: Scala Promises
  */
val promise = Promise[Int]()
val futureFromPromise = promise.future

futureFromPromise.foreach(println)

promise.success(100)
