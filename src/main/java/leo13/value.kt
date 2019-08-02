package leo13

import leo.base.fold
import leo9.*

data class Value(val lineStack: Stack<ValueLine>)
data class ValueLine(val int: Int, val rhs: Value)

fun value(lineStack: Stack<ValueLine>) = Value(lineStack)
fun Value.plus(line: ValueLine) = Value(lineStack.push(line))
fun value(vararg lines: ValueLine): Value = Value(stack()).fold(lines) { plus(it) }
infix fun Int.lineTo(rhs: Value) = ValueLine(this, rhs)

val Value.lastLine get() = lineStack.top
fun Value.line(int: Int): ValueLine = lineStack.get(int)!!

fun Value.access(int: Int): Value = value(lastLine.rhs.line(int))
