package leo21.type

import leo.base.fold
import leo.base.notNullOrError
import leo13.Stack
import leo13.any
import leo13.push
import leo13.stack

sealed class Type
data class StructType(val struct: Struct) : Type()
data class ChoiceType(val choice: Choice) : Type()

data class Struct(val lineStack: Stack<Line>)
data class Choice(val lineStack: Stack<Line>)
data class Arrow(val lhs: Type, val rhs: Type)

sealed class Line
object StringLine : Line()
object DoubleLine : Line()
data class FieldLine(val field: Field) : Line()
data class ArrowLine(val arrow: Arrow) : Line()

data class Field(val name: String, val rhs: Type)

fun type(struct: Struct): Type = StructType(struct)
fun type(choice: Choice): Type = ChoiceType(choice)

val Type.structOrNull get() = (this as? StructType)?.struct
val Type.choiceOrNull get() = (this as? ChoiceType)?.choice

val Line.isString: Boolean get() = this is StringLine
val Line.isDouble: Boolean get() = this is DoubleLine
val Line.arrowOrNull: Arrow? get() = (this as? ArrowLine)?.arrow
val Line.fieldOrNull: Field? get() = (this as? FieldLine)?.field

val Type.struct get() = structOrNull.notNullOrError("not struct")
val Type.choice get() = choiceOrNull.notNullOrError("not choice")

val emptyStruct = Struct(stack())
fun struct(vararg lines: Line) = emptyStruct.fold(lines) { plus(it) }
fun Struct.plus(line: Line) =
	if (lineStack.any { name == line.name }) error("duplicate field")
	else Struct(lineStack.push(line))

fun Type.plus(line: Line): Type = type(struct.plus(line))

val emptyChoice = Choice(stack())
fun choice(vararg lines: Line) = emptyChoice.fold(lines) { plus(it) }
fun Choice.plus(line: Line) =
	if (lineStack.any { name == line.name }) error("duplicate case")
	else Choice(lineStack.push(line))

fun type(vararg lines: Line) = type(struct(*lines))

val stringLine: Line = StringLine
val doubleLine: Line = DoubleLine

val stringType = type(stringLine)
val doubleType = type(doubleLine)

fun line(arrow: Arrow): Line = ArrowLine(arrow)
fun line(field: Field): Line = FieldLine(field)

infix fun String.fieldTo(rhs: Type) = Field(this, rhs)
infix fun String.lineTo(rhs: Type) = line(this fieldTo rhs)

infix fun Type.arrowTo(rhs: Type) = Arrow(this, rhs)
infix fun Type.lineTo(rhs: Type) = line(this arrowTo rhs)

val Line.name: String
	get() =
		when (this) {
			StringLine -> "text"
			DoubleLine -> "number"
			is FieldLine -> field.name
			is ArrowLine -> "function"
		}

fun Type.make(name: String): Type =
	type(name lineTo this)
