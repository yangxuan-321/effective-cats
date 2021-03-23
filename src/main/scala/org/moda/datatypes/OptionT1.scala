package org.moda.datatypes

import cats.data._
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

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
    val c = for {
      // xx <- OptionT(customGreeting(Option.empty[String]))
      xx <- OptionT(customGreeting(Option("123")))
      _ = println("test3 -> 到这里1")
      xx1 <- OptionT.liftF(Future.successful(xx))
      _ = println("test3 -> 到这里2")
      xx2 <- OptionT.fromOption[Future](Option.empty[String])
      _ = println("test3 -> 到这里3")
      xx3 <- OptionT.liftF(Future.successful(xx2 + xx1))
      _ = println("test3 -> 到这里4")
      _ = println("xx3的值 -> {}", xx3)
    } yield xx3
    println(c.getOrElse("青萍剑客"))
  }

  def test4: Unit = {
    val defaultGreeting: Future[String] = Future.successful("hello, there")

    val greeting: Future[String] = customGreeting(Some("123")).flatMap(custom =>
      custom.map(Future.successful).getOrElse(defaultGreeting))

  }

  def test5: Unit = {
    val xc = Option(Future.successful("123"))
    val xc1 = Option(Future.successful("123"))
    val xxx: Option[Future[String]] = xc.map(c => c.map({
      Thread.sleep(5000)
      println("step1")
      _.concat("123")
    }))
    val xxx1: Option[Future[String]] = xc1.map(c => c.map({
      Thread.sleep(5000)
      println("step2")
      _.concat("123")
    }))

    // xxx.collect {case v => v.}
  }

  def test6: Unit ={
    val xc = Future.successful(Option("123"))
    val xc1 = Future.successful(Option("123"))
    val xcx = xc.map(c => c.map({
      Thread.sleep(5000)
      println("step1")
      _.concat("123")
    }))
    val xc1x = xc1.map(c => c.map({
      Thread.sleep(5000)
      println("step2")
      _.concat("123")
    }))

    val d = for {
      a <- xcx
      b <- xc1x
      c <- Future.successful(a.map(_.concat(b.getOrElse(""))))
    } yield c

//    d.onComplete {
//      case Success(value) => println(Either.right(value.getOrElse("")))
//      case Failure(exception) => println(Either.left(exception.getMessage))
//    }

//    Thread.sleep(11111)
    Await.result(d, 0.nanos)
  }

  def customGreeting(s: Option[String]): Future[Option[String]] ={
    Future.successful(s)
  }

  def run(): Unit = {
    test6
    // " => -> <- != "
  }

  run()
}
