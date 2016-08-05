package parser

import argonaut._
import parser.components.{User, Group}

class VKParser(response_parser: ResponseParser) extends Thread {


  private val friends_method = "friends.get"
  private val group_method = "groups.getMembers"

  private val friends_params = Map(
    "order" -> "hints",
    "fields" -> "city,contacts",
    "name_case" -> "nom"
  )

  private val group_params = Map(
    "order" -> "hints",
    "fields" -> "city,contacts",
    "name_case" -> "nom"
  )

  override def run: Unit = {
    while (!Container.parse_ids.isEmpty) {
      val element = Container.parse_ids.peek()
      Container.parse_ids.poll()
      element match {
        case Some(User(u, n, ln, c, hp, h, uid, l)) => handle(element.get.asInstanceOf[User])
        case Some(Group(g, i)) => handle(element.get.asInstanceOf[Group])
        case None =>
        case _ =>
      }

    }
    println("VKParser --")
    Container.vkparser_count -= 1
  }

  private def handle(group: Group): Unit = {
    println(s"Parse id: $group")
    val params = group_params +("group_id" -> group.group_id, "offset" -> group.offSet.toString)
    Container.response_list.add(Map(Some(group) -> HttpExecutor.sendGetRequest(group_method, params)))
  }

  private def handle(user: User): Unit = {
    println(s"Parse id: $user")
    val user_id = "user_id" -> user.uid.toString
    val params = friends_params + user_id
    Container.response_list.add(Map(Some(user) -> HttpExecutor.sendGetRequest(friends_method, params)))
    Container.users_list = Container.users_list + user
  }

  def friendsCount(response: Option[String]): Int = {
    response match {
      case Some(str) =>
        val count = Parse.parseWith(str, _.field("response").flatMap(_.array).getOrElse(List()).size, ans => ans)
        count.asInstanceOf[Int]
      case None => -1
    }
  }
}
