package leo14.typed

import leo.base.fold
import leo.base.notNullOrError
import leo13.*
import leo14.*
import leo14.syntax.Syntax
import leo14.syntax.coreColorString

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

object AnyLine : Line()

data class Choice(val optionStack: Stack<Option>) {
	override fun toString() = scriptLine.toString()
}

data class Field(val string: String, val rhs: Type) {
	override fun toString() = scriptLine.toString()
}

data class Option(val string: String, val rhs: Type) {
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

val impossibleType: Type = type(choice())
val anyLine: Line get() = AnyLine

val nativeLine: Line = NativeLine
fun line(choice: Choice): Line = ChoiceLine(choice)
fun line(field: Field): Line = FieldLine(field)
fun line(string: String): Line = line(string fieldTo emptyType)
fun line(arrow: Arrow): Line = ArrowLine(arrow)

val nativeType = type(nativeLine)

val Stack<Option>.choice get() = Choice(this)
fun choice(vararg options: Option) = stack(*options).choice
fun choice(string: String, vararg strings: String): Choice =
	choice(option(string), *strings.map { option(it) }.toTypedArray())

fun Choice.plus(option: Option) = optionStack.push(option).choice
fun <R> Choice.split(fn: (Choice, Option) -> R): R? =
	optionStack.split { stack, option -> fn(stack.choice, option) }

infix fun String.fieldTo(type: Type) = Field(this, type)
infix fun String.optionTo(type: Type) = Option(this, type)
infix fun String.lineTo(type: Type) = line(this fieldTo type)
infix fun Type.arrowTo(rhs: Type) = Arrow(this, rhs)
fun field(string: String) = string fieldTo type()
fun option(string: String) = string optionTo type()

// TODO: In case of performance problems, add Type.isStatic field.
val Type.isStatic: Boolean get() = lineStack.all { isStatic }
val Line.isStatic
	get() = when (this) {
		is NativeLine -> false
		is FieldLine -> field.isStatic
		is ChoiceLine -> choice.isStatic
		is ArrowLine -> arrow.isStatic
		is AnyLine -> false
	}
val Choice.isStatic get() = false
val Field.isStatic get() = rhs.isStatic
val Option.isStatic get() = rhs.isStatic
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
			is AnyLine -> "script" lineTo script()
		}

val Choice.scriptLine
	get() =
		"choice".lineTo(script().fold(optionStack.reverse) { plus(it.scriptLine) })

val Option.scriptLine
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

val Type.onlyLine
	get() =
		onlyLineOrNull.notNullOrError("$this.onlyLine")

val Line.arrowOrNull
	get() =
		(this as? ArrowLine)?.arrow

val Line.fieldOrNull
	get() =
		(this as? FieldLine)?.field

val Line.choiceOrNull
	get() =
		(this as? ChoiceLine)?.choice

val Line.choice
	get() =
		choiceOrNull.notNullOrError("$this.choice")

fun Type.checkIs(other: Type): Type =
	apply { if (this != other) error("$this as $other") }

val Choice.countIndex: Index
	get() =
		index0.fold(optionStack) { next }

val Type.coreString: String
	get() =
		processorString {
			map(Syntax::coreColorString).process(this@coreString, defaultDictionary)
		}

const val textName = "text"
val textLine = textName lineTo nativeType
val textType = type(textLine)

const val numberName = "number"
val numberLine = numberName lineTo nativeType
val numberType = type(numberLine)

val Dictionary.textLine get() = text lineTo nativeType
val Dictionary.textType get() = type(textLine)

val Dictionary.numberLine get() = number lineTo nativeType
val Dictionary.numberType get() = type(numberLine)
