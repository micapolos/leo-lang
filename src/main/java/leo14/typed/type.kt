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

data class FieldLine(val field: Field) : Line() {
	override fun toString() = scriptLine.toString()
}

data class ChoiceLine(val choice: Choice) : Line() {
	override fun toString() = scriptLine.toString()
}

data class ArrowLine(val arrow: Arrow) : Line() {
	override fun toString() = scriptLine.toString()
}

data class Choice(val caseStack: Stack<Case>) {
	override fun toString() = scriptLine.toString()
}

data class Field(val string: String, val rhs: Type) {
	override fun toString() = scriptLine.toString()
}

data class Case(val string: String, val rhs: Type) {
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
fun Type.plus(field: Field) = plus(line(field))
fun type(field: Field, vararg fields: Field) = emptyType.plus(field).fold(fields) { plus(it) }
fun type(string: String) = type(string fieldTo type())
val Type.lineLinkOrNull: Link<Type, Line>?
	get() =
		lineStack.linkOrNull?.let { link ->
			link.stack.type linkTo link.value
		}

val nativeLine: Line = NativeLine
fun line(choice: Choice): Line = ChoiceLine(choice)
fun line(field: Field): Line = FieldLine(field)
fun line(string: String): Line = line(string fieldTo emptyType)
fun line(arrow: Arrow): Line = ArrowLine(arrow)

val nativeType = type(nativeLine)

val Stack<Case>.choice get() = Choice(this)
fun choice(vararg cases: Case) = stack(*cases).choice
fun choice(string: String, vararg strings: String): Choice =
	choice(case(string), *strings.map { case(it) }.toTypedArray())

fun Choice.plus(case: Case) = caseStack.push(case).choice
fun <R> Choice.split(fn: (Choice, Case) -> R): R? =
	caseStack.split { stack, case -> fn(stack.choice, case) }

infix fun String.fieldTo(type: Type) = Field(this, type)
infix fun String.caseTo(type: Type) = Case(this, type)
infix fun String.lineTo(type: Type) = line(this fieldTo type)
infix fun Type.arrowTo(rhs: Type) = Arrow(this, rhs)
fun field(string: String) = string fieldTo type()
fun case(string: String) = string caseTo type()

// TODO: In case of performance problems, add Type.isStatic field.
val Type.isStatic: Boolean get() = lineStack.all { isStatic }
val Line.isStatic
	get() = when (this) {
		is NativeLine -> false
		is FieldLine -> field.isStatic
		is ChoiceLine -> choice.isStatic
		is ArrowLine -> arrow.isStatic
	}
val Choice.isStatic get() = false
val Field.isStatic get() = rhs.isStatic
val Case.isStatic get() = rhs.isStatic
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
			is FieldLine -> field.scriptLine
			is ChoiceLine -> choice.scriptLine
			is ArrowLine -> arrow.scriptLine
		}

val Choice.scriptLine
	get() =
		"choice".lineTo(script().fold(caseStack.reverse) { plus(it.scriptLine) })

val Case.scriptLine
	get() =
		string lineTo rhs.script

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
	apply { if (this != other) error("$this as $other") }

val Choice.countIndex: Index
	get() =
		index0.fold(caseStack) { next }
