package parser.phone

import parser.components.{Terminate, CodeRead, Read, State}

import scala.collection.mutable
import scala.util.control.Breaks._



object PhoneNumber {

   def check(number: List[Char]): Boolean = {

    val value = new mutable.StringBuilder()

    var currentState: State = Read

    var result = number.nonEmpty

    breakable {
      for (element <- number) {
        currentState match {
          case Read => read(element)
          case CodeRead => codeRead(element)
          case Terminate =>
            break()
        }
      }
    }

    def read(c: Char): Unit = {
      c match {
        case '+' =>
          if (value.nonEmpty)
            changeState(Terminate)
        case ' ' =>
          if (value.nonEmpty && value.takeRight(1).last == ' ')
            changeState(Terminate)
        case '-' =>
          if (value.nonEmpty && value.takeRight(1).last == '-')
            changeState(Terminate)
        case '(' => changeState(CodeRead)
        case _ =>
          if (c < 48 || c > 57)
            changeState(Terminate)
      }
      value += c
    }

    def codeRead(c: Char): Unit = {
      c match {
        case ')' => changeState(Read)
        case _ =>
          if (c < 48 && c > 57)
            changeState(Terminate)
      }
      value += c
      if (value.length - value.indexOf(')') > 4)
        changeState(Terminate)
    }

    def changeState(state: State): Unit = {
      if(state == Terminate)
        result = false
      currentState = state
    }

    result
  }


}
