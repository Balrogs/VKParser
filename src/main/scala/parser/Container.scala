package parser

import java.util.concurrent.ConcurrentLinkedQueue

import parser.components.User


object Container {
  var isToSearchInFriends = false
  var users_list: Set[User] = Set[User]()
  var vkparser_count: Int= 0
  var response_list: ConcurrentLinkedQueue[Map[Option[Any],Option[String]]] = new ConcurrentLinkedQueue[Map[Option[Any],Option[String]]]()
  var parse_ids: ConcurrentLinkedQueue[Option[Any]] = new ConcurrentLinkedQueue[Option[Any]]()
}
