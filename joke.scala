//> using platform "native"
//> using lib "org.http4s::http4s-ember-client::0.23.19-RC3"
//> using lib "org.http4s::http4s-circe::0.23.19-RC3"
//> using lib "com.armanbilge::epollcat::0.1.4"

import cats.effect.*
import epollcat.*
import io.circe.Json
import org.http4s.circe.*
import org.http4s.implicits.*
import org.http4s.ember.client.EmberClientBuilder

object Joke extends EpollApp.Simple:

  def run = EmberClientBuilder.default[IO].build.use { client =>
    client.expect[Json](uri"https://icanhazdadjoke.com").flatMap(IO.println)
  }
