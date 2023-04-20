package io.chrisdavenport.sndockertest

import cats.effect._
import io.circe.Json
import org.http4s.circe._
import org.http4s.implicits._
import org.http4s.ember.client.EmberClientBuilder

import io.chrisdavenport.crossplatformioapp.CrossPlatformIOApp

object Main extends CrossPlatformIOApp {

  def run(args: List[String]): IO[ExitCode] = EmberClientBuilder.default[IO].build.use{
    client => 
    client.expect[Json](uri"https://icanhazdadjoke.com")
      .flatTap(IO.println)
  }.as(ExitCode.Success)

}