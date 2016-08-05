package parser.components

import argonaut.Argonaut._
import argonaut._

/**
 * Created by igor on 29.07.16.
 */



case class Lists(number: Int)

object Lists {
  // Define codecs easily from case classes
  implicit def ListsCodecJson: CodecJson[Lists] =
    casecodec1(Lists.apply, Lists.unapply)("number")
}

case class User(uid : Int, first_name: String, last_name: String, city : Option[Int], home_phone: Option[String], hidden: Option[Int], user_id: Option[Int], lists: Option[List[Lists]])

object User {
  implicit def UserCodecJson: CodecJson[User] =
    casecodec8(User.apply, User.unapply)("uid", "first_name", "last_name", "city", "home_phone", "hidden", "user_id", "lists")
}