package leo13.js2

import leo13.Stack
import leo13.stack
import leo13.toList

data class Args(val stack: Stack<String>)

fun args(vararg strings: String) = Args(stack(*strings))
val Args.code get() = stack.toList().joinToString()