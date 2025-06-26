package $package$.app

import cats.effect.{ExitCode, IO, IOApp}
import $package$.core.types.{Message, Name}
import $package$.core.error.ErrorMessage

object MyAppMain extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = for {
    _    <- IO(println("======================="))
    name <- IO.fromOption(args.headOption.map(Name(_)))(ErrorMessage("Please pass an argument for name."))
    _    <- GreetingApp[IO].run(Message("Hello"), name)
    _    <- IO(println("======================="))
  } yield ExitCode.Success
}
