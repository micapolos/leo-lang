package leo19.value

import leo.base.fold
import leo.base.seq
import leo13.Stack
import leo13.push
import leo13.stack

data class Scope(val stack: Stack<Value>)

val emptyScope = Scope(stack())
fun Scope.push(value: Value) = Scope(stack.push(value))
fun scope(vararg values: Value): Scope = emptyScope.fold(values.toList().seq) { push(it) }

