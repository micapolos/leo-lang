package leo23.value

import leo.stak.Stak
import leo.stak.top

typealias Scope = Stak<Value>

fun Scope.arg(index: Int): Value = top(index)!!
val Scope.arg0: Value get() = arg(0)
val Scope.arg1: Value get() = arg(1)
val Scope.arg2: Value get() = arg(2)
