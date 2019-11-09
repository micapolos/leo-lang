package leo14.typed

import leo.base.fold
import leo13.*
import leo14.*

data class Type(val lineStack: Stack<Line>) {
	override fun toString() = script.toString()
}

sealed class Line

object NativeLine : Line() {
	override fun toString() = scriptLine.toString()
}

data class ChoiceLine(val choice: Choice) : Line() {
	override fun toString() = scriptLine.toString()
}

data class ArrowLine(val arrow: Arrow) : Line() {
	override fun toString() = scriptLine.toString()
}

data class Choice(val fieldStackLink: StackLink<Field>) {
	override fun toString() = scriptLine.toString()
}

data class Field(val string: String, val rhs: Type) {
	override fun toString() = scriptLine.toString()
}

data class Arrow(val lhs: Type, val rhs: Type) {
	override fun toString() = scriptLine.toString()
}

val emptyType = Type(stack())
val Stack<Line>.type get() = Type(this)
fun type(vararg lines: Line) = stack(*lines).type
fun type(choice: Choice) = type(line(choice))
fun Type.plus(line: Line) = lineStack.push(line).type
fun Type.plus(field: Field) = plus(line(choice(field)))
fun type(field: Field, vararg fields: Field) = emptyType.plus(field).fold(fields) { plus(it) }
fun type(string: String) = type(string fieldTo type())
val Type.lineLinkOrNull: Link<Type, Line>?
	get() =
		lineStack.linkOrNull?.let { link ->
			link.stack.type linkTo link.value
		}

val nativeLine: Line = NativeLine
fun line(choice: Choice): Line = ChoiceLine(choice)
fun line(field: Field): Line = line(choice(field))
fun line(string: String): Line = line(string fieldTo emptyType)
fun line(arrow: Arrow): Line = ArrowLine(arrow)

val nativeType = type(nativeLine)

val StackLink<Field>.choice get() = Choice(this)
val Stack<Field>.choiceOrNull get() = linkOrNull?.choice
fun choice(field: Field, vararg fields: Field) = stackLink(field, *fields).choice
fun choice(string: String, vararg strings: String): Choice =
	choice(field(string), *strings.map { field(it) }.toTypedArray())
fun Choice.plus(field: Field) = fieldStackLink.push(field).choice
fun Choice?.orNullPlus(field: Field): Choice = this?.plus(field) ?: choice(field)
val Choice.onlyFieldOrNull: Field? get() = fieldLink.onlyHeadOrNull
val Choice.lastField get() = fieldLink.head
val Choice.previousChoiceOrNull get() = fieldLink.tail
val Choice.fieldLink
	get() =
		when (fieldStackLink.stack) {
			is EmptyStack -> null
			is LinkStack -> fieldStackLink.stack.link.choice
		} linkTo fieldStackLink.value

fun <R> Choice.split(fn: (Choice?, Field) -> R): R =
	fn(fieldStackLink.stack.choiceOrNull, fieldStackLink.value)

infix fun String.fieldTo(type: Type) = Field(this, type)
infix fun String.lineTo(type: Type) = line(choice(fieldTo(type)))
infix fun Type.arrowTo(rhs: Type) = Arrow(this, rhs)
fun field(string: String) = string fieldTo type()

// TODO: In case of performance problems, add Type.isStatic field.
val Type.isStatic: Boolean get() = lineStack.all { isStatic }
val Line.isStatic
	get() = when (this) {
		is NativeLine -> false
		is ChoiceLine -> choice.isStatic
		is ArrowLine -> arrow.isStatic
	}
val Choice.isStatic get() = fieldLink.head.isStatic && fieldLink.tail == null
val Field.isStatic get() = rhs.isStatic
val Arrow.isStatic get() = rhs.isStatic || lhs.isStatic

val Type.isEmpty get() = lineStack.isEmpty

// === Scripting

val Type.script: Script
	get() =
		script().fold(lineStack.reverse) { plus(it.scriptLine) }

val Line.scriptLine: ScriptLine
	get() =
		when (this) {
			is NativeLine -> "native" lineTo script()
			is ChoiceLine -> choice.scriptLine
			is ArrowLine -> arrow.scriptLine
		}

val Choice.scriptLine
	get() =
		onlyFieldOrNull?.run { scriptLine }
			?: "choice".lineTo(script().fold(stack(fieldStackLink).reverse) { plus(it.scriptLine) })

val Field.scriptLine
	get() =
		string lineTo rhs.script

val Arrow.scriptLine
	get() =
		"function" lineTo script(
			"takes" fieldTo lhs.script,
			"gives" fieldTo rhs.script)

val Type.onlyLineOrNull
	get() =
		lineStack.onlyOrNull

val Line.arrowOrNull
	get() =
		(this as? ArrowLine)?.arrow

val Line.choiceOrNull
	get() =
		(this as? ChoiceLine)?.choice

fun Type.checkIs(other: Type): Type =
	apply { if (this != other) error("$this as other") }