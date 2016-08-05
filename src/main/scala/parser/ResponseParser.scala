package parser

import argonaut._
import parser.components.{User, GroupResponse, UserResponse, Group}


class ResponseParser extends Thread {


  override def run(): Unit = {

    while (Container.vkparser_count > 0 || !Container.response_list.isEmpty || !Container.parse_ids.isEmpty) {
      if(!Container.response_list.isEmpty){
        val element = Container.response_list.peek()
        Container.response_list.poll()
        element.head._2 match {
          case Some(s) =>
            element.head._1 match {
              case Some(User(u, n, ln, c, hp, h, uid, l)) => parse_user_response(Map(element.head._1.get.asInstanceOf[User] -> s))
              case Some(Group(g, i)) => parse_group_response(Map(element.head._1.get.asInstanceOf[Group] -> s))
              case None =>
              case _ =>
            }
          case None =>
        }
      }
    }
  }

  private def parse_user_response(response: Map[User, String]): Unit = {
    val decoded_response = Parse.decodeOption[UserResponse](response.head._2)
    decoded_response match {
      case Some(r) =>
        r.resp.foreach(u => Container.users_list = Container.users_list + u)
        if (Container.isToSearchInFriends) {
          r.resp.foreach(u => Container.parse_ids.offer(Some(u)))
        }
      case None =>
    }
  }


  private def parse_group_response(response: Map[Group, String]): Unit = {
    Parse.decodeOption[GroupResponse](response.head._2) match {
      case Some(r) =>
        if (r.response.count.get > 1000 && response.head._1.offSet == 0) {
          var offset = 0

          while (r.response.count.get - offset > 1000) {
            offset += 1000
            val group = new Group(response.head._1.group_id, offset)
            group.offSet = offset
            Container.parse_ids.offer(Some(group))
          }
        }
        r.response.users.foreach(u => Container.users_list = Container.users_list + u)
        if (Container.isToSearchInFriends) {
          r.response.users.foreach(u => Container.parse_ids.offer(Some(u)))
        }
      case None =>
    }
  }
}
