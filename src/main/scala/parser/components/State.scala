package parser.components

/**
 * Created by igor on 03.08.16.
 */
sealed trait State

object Read extends State

object CodeRead extends State

object Terminate extends State

