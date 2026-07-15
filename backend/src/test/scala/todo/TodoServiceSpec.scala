package todo

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TodoServiceSpec extends AnyFlatSpec with Matchers:

  val service = new TodoService

  "add" should "add a new item" in
    val item = service.add("Buy milk")
    item.text shouldBe "Buy milk"
    item.completed shouldBe false
    service.getAll.size shouldBe 1

  it should "return items in order" in
    service.add("First")
    service.add("Second")
    service.getAll.map(_.text) shouldBe List("First", "Second")

  "toggle" should "toggle an existing item" in
    val item = service.add("Toggle me")
    val toggled = service.toggle(item.id).get
    toggled.completed shouldBe true
    service.getAll.find(_.id == item.id).get.completed shouldBe true

  it should "return None for unknown id" in
    service.toggle("unknown") shouldBe None

  "delete" should "remove an existing item" in
    val item = service.add("Delete me")
    service.delete(item.id) shouldBe true
    service.getAll.find(_.id == item.id) shouldBe None

  it should "return false for unknown id" in
    service.delete("unknown") shouldBe false
