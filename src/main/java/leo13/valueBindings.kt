package leo13

import leo9.Stack
import leo9.nonEmptyStack
import leo9.push
import leo9.stack

data class ValueBindings(val stack: Stack<Value>)

val Stack<Value>.valueBindings get() = ValueBindings(this)
fun ValueBindings.push(value: Value) = stack.push(value).valueBindings
fun valueBindings(vararg values: Value) = stack(*values).valueBindings
fun bindings(value: Value, vararg values: Value) = nonEmptyStack(value, *values).valueBindings
