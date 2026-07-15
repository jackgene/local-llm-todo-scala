package todo

import cats.effect.*
import cats.effect.unsafe.implicits.global
import cats.syntax.all.*
import io.circe.parser.*
import io.circe.syntax.*
import org.http4s.*
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s.*

object TodoService:
  class Live:
    private var items: List[ListItem] = List.empty

    def getAll: List[ListItem] = items

    def add(text: String): ListItem =
      val item = ListItem(java.util.UUID.randomUUID().toString, text, false)
      items = items :+ item
      item

    def toggle(id: String): Option[ListItem] =
      items.find(_.id == id).map { item =>
        val updated = item.copy(completed = !item.completed)
        items = items.map {
          case `item` => updated
          case other  => other
        }
        updated
      }

    def delete(id: String): Boolean =
      val sizeBefore = items.size
      items = items.filterNot(_.id == id)
      items.size < sizeBefore

object TodoServer:

  val service = TodoService.Live()

  def handleRequest(req: Request[IO]): IO[Response[IO]] =
    val path = req.uri.path.toString
    req.method match
      case Method.GET if path == "/api/todos" =>
        IO(Response(Status.Ok).withEntity(service.getAll))
      case Method.POST if path == "/api/todos" =>
        for
          json <- req.as[io.circe.Json]
          text = json.hcursor.downField("text").as[String].toOption.getOrElse("")
          item <- IO(service.add(text))
        yield Response(Status.Created).withEntity(item)
      case Method.PUT if path.startsWith("/api/todos/") =>
        val id = path.stripPrefix("/api/todos/")
        service.toggle(id) match
          case Some(item) => IO(Response(Status.Ok).withEntity(item))
          case None       => IO(Response(Status.NotFound))
      case Method.DELETE if path.startsWith("/api/todos/") =>
        val id = path.stripPrefix("/api/todos/")
        if service.delete(id) then IO(Response(Status.NoContent))
        else IO(Response(Status.NotFound))
      case _ =>
        IO(Response(Status.NotFound))

  val routes: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req => handleRequest(req)
  }

  def main(args: Array[String]): Unit =
    val program = EmberServerBuilder
      .default[IO]
      .withPort(Port.fromInt(8080).get)
      .withHost(Host.fromString("0.0.0.0").get)
      .withHttpApp(routes.orNotFound)
      .build

    program.use(_ => IO.never).unsafeRunSync()
