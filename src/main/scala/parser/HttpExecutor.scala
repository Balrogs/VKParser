package parser

import java.net.SocketTimeoutException

import scalaj.http.Http

object HttpExecutor {

  val http_path = "https://api.vk.com/method/"

  def sendGetRequest(method_name: String, params: Map[String, String]): Option[String] = {
    try {
      val result = Http(http_path + method_name).params(params).asString
      if (result.isSuccess) {
        Some(result.body)
      } else None
    } catch {
      case e :SocketTimeoutException => sendGetRequest(method_name, params)
    }
  }
}
