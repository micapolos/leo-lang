package leo13

import leo9.Stack
import leo9.stack

object Previous

val previous = Previous

data class Argument(val previousStack: Stack<Previous>)

val Stack<Previous>.argument get() = Argument(this)
fun argument(vararg previouses: Previous) = stack(*previouses).argument
