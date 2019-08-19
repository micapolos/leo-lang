package leo13

import leo9.*

data class Value(val lineStack: Stack<ValueLine>)
data class ValueLine(val int: Int, val rhs: Value)

val Stack<ValueLine>.value get() = Value(this)
fun Value.plus(line: ValueLine) = lineStack.push(line).value
fun value(vararg lines: ValueLine): Value = stack(*lines).value
infix fun Int.lineTo(rhs: Value) = ValueLine(this, rhs)

val Value.lastLine get() = lineStack.top
fun Value.line(int: Int): ValueLine = lineStack.get(int)!!

fun Value.access(int: Int): Value = value(lastLine.rhs.line(int))

val Value.headOrNull get() = lineStack.linkOrNull?.value?.onlyStack?.value
val Value.tailOrNull get() = lineStack.linkOrNull?.stack?.value
val Value.rhsOrNull get() = lineStack.linkOrNull?.value?.rhs
