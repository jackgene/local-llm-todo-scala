package todo

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import io.circe.parser.*
import io.circe.syntax.*

class ListItemSpec extends AnyFlatSpec with Matchers:

  "ListItem" should "encode to JSON correctly" in
    val item = ListItem("1", "Test", false)
    val json = item.asJson
    json.hcursor.downField("id").as[String] shouldBe Right("1")
    json.hcursor.downField("text").as[String] shouldBe Right("Test")
    json.hcursor.downField("completed").as[Boolean] shouldBe Right(false)

  it should "decode from JSON correctly" in
    val json = """{"id":"2","text":"Hello","completed":true}"""
    val result = decode[ListItem](json)
    result shouldBe a[Right[_, ?]]
    result.toOption.get shouldBe ListItem("2", "Hello", true)
