package todo

import io.circe.generic.semiauto.*
import io.circe.{Decoder, Encoder}

case class ListItem(id: String, text: String, completed: Boolean)

object ListItem:
  given Encoder[ListItem] = deriveEncoder
  given Decoder[ListItem] = deriveDecoder
