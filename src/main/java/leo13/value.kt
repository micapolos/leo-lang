package leo13

import leo.base.Empty
import leo.base.empty
import leo.base.fold

sealed class Value
data class EmptyValue(val empty: Empty) : Value()
data class LinkValue(val link: ValueLink) : Value()

data class ValueLink(val lhs: Value, val line: ValueLine)
data class ValueLine(val int: Int, val rhs: Value)

val Value.empty get() = (this as EmptyValue).empty
val Value.link get() = (this as LinkValue).link

fun value(empty: Empty): Value = EmptyValue(empty)
fun value(link: ValueLink): Value = LinkValue(link)
fun link(lhs: Value, line: ValueLine) = ValueLink(lhs, line)
infix fun Int.lineTo(rhs: Value) = ValueLine(this, rhs)
fun Value.plus(line: ValueLine) = value(link(this, line))
fun value(vararg lines: ValueLine): Value = value(empty).fold(lines) { plus(it) }

fun Value.get(int: Int): Value = value(link.line.rhs.link.getLine(int))
tailrec fun ValueLink.getLine(int: Int): ValueLine =
	if (int == 0) line
	else lhs.link.getLine(int.dec())