package leo13

import leo.base.Empty
import leo.base.Nat
import leo.base.empty
import leo.base.fold

sealed class Value
data class EmptyValue(val empty: Empty) : Value()
data class BodyValue(val body: ValueBody) : Value()

sealed class ValueBody
data class LinkValueBody(val link: ValueLink) : ValueBody()
data class ChoiceValueBody(val choice: ValueChoice) : ValueBody()

data class ValueChoice(val nat: Nat, val rhs: Value)
data class ValueLink(val lhs: Value, val line: ValueLine)
data class ValueLine(val int: Int, val rhs: Value)

val Value.empty get() = (this as EmptyValue).empty
val Value.body get() = (this as BodyValue).body
val ValueBody.link get() = (this as LinkValueBody).link
val ValueBody.choice get() = (this as ChoiceValueBody).choice

fun value(empty: Empty): Value = EmptyValue(empty)
fun value(body: ValueBody): Value = BodyValue(body)
fun body(link: ValueLink): ValueBody = LinkValueBody(link)
fun body(choice: ValueChoice): ValueBody = ChoiceValueBody(choice)
fun link(lhs: Value, line: ValueLine) = ValueLink(lhs, line)
infix fun Int.lineTo(rhs: Value) = ValueLine(this, rhs)
fun Value.plus(line: ValueLine) = value(body(link(this, line)))
fun value(vararg lines: ValueLine): Value = value(empty).fold(lines) { plus(it) }
