package $package$.core

import cats.effect.IO
import extras.hedgehog.ce3.syntax.runner.*
import hedgehog.*
import hedgehog.runner.*
import types.*

object GreetingSpec extends Properties {
  override def tests: List[Test] = List(
    property("""Greeting(Message).get(Name) should return "message name"""", testGreet)
  )

  def testGreet: Property = for {
    name    <- Gen.string(Gen.alpha, Range.linear(3, 10)).map(Name(_)).log("name")
    message <- Gen.string(Gen.unicode, Range.linear(5, 15)).map(Message(_)).log("message")
  } yield runIO {
    val greeting = Greeting[IO](message)
    val expected = Message(s"\${message.value} \${name.value}")
    greeting.get(name).map { actual =>
      actual ==== expected
    }
  }
}
