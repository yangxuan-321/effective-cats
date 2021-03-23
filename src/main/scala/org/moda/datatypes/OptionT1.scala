package org.moda.datatypes

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import cats.effect.{ExitCode, IO, IOApp}

object OptionT1 extends App {
  def test1(): Unit ={
    val customGreeting: Future[Option[String]] =
      Future.successful(Some("welcome back, Lola"))
    // 操作需要 两次 map
    val a: Future[Option[String]] = customGreeting.map(_.map(_ + "!"))
    val c: Future[String] = a.map(_.getOrElse("hello there!"))

    // 如果 使用 for 则可以少一层 map。
    val cc = for {
      ii <- customGreeting
      aa <- Future.successful(ii.map(_ + "!"))
      bb <- Future.successful(aa.getOrElse("hello there!"))
    }yield bb


  }


  def run(): Unit = {
    println("123")
  }

  run()
}
