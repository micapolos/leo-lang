package leo13.untyped.compiler

import leo9.Stack
import leo9.pushAll
import leo9.stack

data class Expression(val lhsFunctionOrNull: Function?, val rhsOpStack: Stack<Op>)

fun expression(lhsFunctionOrNull: Function? = null) = Expression(lhsFunctionOrNull, stack())
fun Expression.plus(vararg ops: Op) = Expression(lhsFunctionOrNull, rhsOpStack.pushAll(*ops))
