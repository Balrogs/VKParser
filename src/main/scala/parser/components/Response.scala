package parser.components

import argonaut.Argonaut._
import argonaut.CodecJson

case class UserResponse(resp: List[User])

object UserResponse {
  implicit def UserResponseCodecJson: CodecJson[UserResponse] =
    casecodec1(UserResponse.apply, UserResponse.unapply)("response")
}

case class GroupResponse(response: Response)

object GroupResponse {
  implicit def GroupResponseCodecJson: CodecJson[GroupResponse] =
    casecodec1(GroupResponse.apply, GroupResponse.unapply)("response")
}

case class Response(count: Option[Int],users: List[User])

object Response {
  implicit def ResponseCodecJson: CodecJson[Response] =
    casecodec2(Response.apply, Response.unapply)("count", "users")
}