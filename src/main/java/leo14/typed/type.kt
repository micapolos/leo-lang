package leo14.typed

import leo.base.fold
import leo13.*

data class Type(val lineStack: Stack<Line>) {
	override fun toString() = lineStack.toList().joinToString(".")
}

sealed class Line

object NativeLine : Line() {
	override fun toString() = "native"
}

data class ChoiceLine(val choice: Choice) : Line() {
	override fun toString() = "$choice"
}

data class ArrowLine(val arrow: Arrow) : Line() {
	override fun toString() = "$arrow"
}

data class Choice(val fieldStack: Stack<Field>) {
	override fun toString() =
		fieldStack.onlyOrNull
			?.let { "$it" }
			?: "choice(${fieldStack.toList().joinToString(".")})"
}

data class Field(val string: String, val rhs: Type) {
	override fun toString() = "$string($rhs)"
}

data class Arrow(val lhs: Type, val rhs: Type) {
	override fun toString() = "function(from($lhs).to($rhs))"
}

val emptyType = Type(stack())
val Stack<Line>.type get() = Type(this)
fun type(vararg lines: Line) = stack(*lines).type
fun Type.plus(line: Line) = lineStack.push(line).type
fun Type.plus(field: Field) = plus(line(choice(field)))
fun type(field: Field, vararg fields: Field) = emptyType.plus(field).fold(fields) { plus(it) }

val nativeLine: Line = NativeLine
fun line(choice: Choice): Line = ChoiceLine(choice)
fun line(arrow: Arrow): Line = ArrowLine(arrow)

val nativeType = type(nativeLine)

val emptyChoice = Choice(stack())
val Stack<Field>.choice get() = Choice(this)
fun choice(vararg fields: Field) = stack(*fields).choice
fun Choice.plus(field: Field) = fieldStack.push(field).choice

infix fun String.fieldTo(type: Type) = Field(this, type)
infix fun Type.arrowTo(rhs: Type) = Arrow(this, rhs)
