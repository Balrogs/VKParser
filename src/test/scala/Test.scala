import junit.framework.TestCase
import org.junit.Assert._

import parser._

import scalaj.http.HttpResponse

class Test extends TestCase{

  def testSendRequest(): Unit ={
    val params : Map[String, String] = Map("user_ids" -> "193614511")
    val result = HttpExecutor.sendGetRequest("users.get", params)
    assertTrue(result.get.contains("193614511"))
  }

  def testCheckFriendsCount(): Unit ={
  }

}
