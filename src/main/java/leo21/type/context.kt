package leo21.type

import leo13.Stack
import leo13.push
import leo13.stack

data class Context(val recursiveStack: Stack<Recursive>)

val emptyContext = Context(stack())
fun Context.plus(recursive: Recursive) = Context(recursiveStack.push(recursive))
