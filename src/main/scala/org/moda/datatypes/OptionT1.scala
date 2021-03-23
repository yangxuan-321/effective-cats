package org.moda.datatypes

import cats._
import cats.data._
import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import cats.syntax.functor._
import cats.syntax.applicative._
import cats.{Id, Monad}

object OptionT1 extends App {
  def test1: Unit ={
    // 操作需要 两次 map
    val a: Future[Option[String]] = customGreeting(Some("welcome back, Lola")).map(_.map(_ + "!"))
    val c: Future[String] = a.map(_.getOrElse("hello there!"))
    println(c)
  }

  def test2: Unit ={
    // 如果 使用 for 则可以少一层 map。
    val cc = for {
      ii <- customGreeting(Some("welcome back, Lola"))
      aa <- Future.successful(ii.map(_ + "!"))
      bb <- Future.successful(aa.getOrElse("hello there!"))
    }yield bb
    println(cc)
  }

  def test3(): Unit ={

    // 如果 使用 OptionT
    // val c = v.map((c: String) => c)
    val _: OptionT[Future, String] = for {
      xx <- OptionT(customGreeting(Option.empty[String]))
      _ = println("test3 -> 到这里1")
      xx1 <- OptionT.liftF(Future.successful(xx))
      _ = println("test3 -> 到这里2")
      xx2 <- OptionT.fromOption[Future](Option.empty[String])
      _ = println("test3 -> 到这里3")
      xx3 <- OptionT.liftF(Future.successful(xx2 + xx1))
      _ = println("test3 -> 到这里4")
      _ = println("xx3的值 -> {}", xx3)
    } yield xx3
  }

  def customGreeting(s: Option[String]): Future[Option[String]] ={
    Future.successful(s)
  }

  def run(): Unit = {
    test3
    // " => -> <- != "
  }

  run()
}
