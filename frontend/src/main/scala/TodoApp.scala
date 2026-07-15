package todo

import cats.effect.*
import cats.effect.unsafe.implicits.global
import tyrian.*
import tyrian.Html.*
import tyrian.Cmd
import tyrian.Sub
import scala.scalajs.js.annotation.*

@JSExportTopLevel("TodoApp")
object TodoApp extends TyrianIOApp:

  type Model = List[ListItem]

  sealed trait Msg
  case class AddItem(text: String) extends Msg
  case class ToggleItem(id: String) extends Msg
  case class DeleteItem(id: String) extends Msg

  val init: (Model, Cmd[IO, Msg]) =
    (List.empty, Cmd.None)

  def update(model: Model, msg: Msg): (Model, Cmd[IO, Msg]) =
    msg match
      case AddItem(text) if text.nonEmpty =>
        val newItem = ListItem(java.util.UUID.randomUUID().toString, text, false)
        (model :+ newItem, Cmd.None)
      case ToggleItem(id) =>
        val updated = model.map {
          case item if item.id == id => item.copy(completed = !item.completed)
          case other                 => other
        }
        (updated, Cmd.None)
      case DeleteItem(id) =>
        (model.filterNot(_.id == id), Cmd.None)

  def view(model: Model): Html[Msg] =
    div(cls := "app")(
      h1("Todo List"),
      ul(cls := "todo-list")(
        model.map { item =>
          li(cls := s"todo-item ${if item.completed then "completed" else ""}")(
            span(item.text),
            button(cls := "delete-btn")(onClick(_ => DeleteItem(item.id)), span("✕"))
          )
        }
      )
    )

  val subscriptions: Model => Sub[IO, Msg] = _ => Sub.None

  val run: IO[Nothing] => Unit = _.unsafeRunAndForget()
