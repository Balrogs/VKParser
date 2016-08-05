import java.io.{FileNotFoundException, File}

import parser._
import parser.components._
import parser.phone.PhoneNumber

import scala.io.Source

object Main {
  val NUMBER_OF_THREADS = 3
  var output_file_path = "result"

  def main(args: Array[String]): Unit = {

   args.length match{
     case 2 =>
       readFromFile(args(0))
       output_file_path = args(1)
       waitResult()
     case _ =>
       println("Usage: vkparser <input file path> <output file path>")
    }
  }

  private def waitResult(): Unit ={

    val response_parser = new ResponseParser()
    response_parser.start()

    while (response_parser.isAlive) {
      val count = Container.vkparser_count
      if (count < NUMBER_OF_THREADS && Container.parse_ids.size() > count) {
        startNewVKParser(response_parser)
        println("VKParser ++")
      }
    }

    var result = Container.users_list
    result = filter(result)

    printResultIntoFile(result)

    println("Task complete !!!")
  }

  private def startNewVKParser(response_parser : ResponseParser): Unit ={
    val vk_parser = new VKParser(response_parser)
    vk_parser.setDaemon(true)
    vk_parser.start()
    Container.vkparser_count += 1
  }

  private def readFromFile(path:String): Unit ={
    try {
      for (line <- Source.fromFile(path).getLines()) {
        Container.parse_ids.offer(Some(Group(line, 0)))
      }
    }
    catch{
      case e:FileNotFoundException => println("Can't find input file!")
        System.exit(0)
    }
  }

  private def filter(result: Set[User]): Set[User] = {
    result.filter(u => {
      u.home_phone match {
        case Some(str) =>
          str.length() > 9 && str.distinct.size > 3 && PhoneNumber.check(str.toList)
        case None => false
      }
    })
  }

  private def printResultIntoFile(result: Set[User]): Unit = {
    try {
      val pw = new java.io.FileWriter(new File(output_file_path), true)
      result.foreach(e => pw.write(e.uid.toString + " " + e.first_name + " " + e.last_name + " " + e.home_phone.get + "\n"))
      pw.close()
    }
    catch{
      case e:FileNotFoundException => println("Can't find output file!")
        System.exit(0)
    }
  }

}
