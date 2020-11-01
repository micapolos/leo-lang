package leo21.type

import leo13.Link
import leo13.Stack
import leo13.linkOrNull
import leo13.linkTo
import leo13.push
import leo13.stack

data class Type(val lineStack: Stack<Line>)

sealed class Line
object StringLine : Line()
object DoubleLine : Line()
data class FieldLine(val field: Field) : Line()

data class Field(val name: String, val rhs: Type)

val Type.linkOrNull: Link<Type, Line>?
	get() =
		lineStack.linkOrNull?.let { link ->
			link.stack.type linkTo link.value
		}

val Stack<Line>.type: Type get() = Type(this)
val nilType = stack<Line>().type
fun Type.plus(line: Line) = lineStack.push(line).type
fun type(vararg lines: Line) = stack(*lines).type

val stringLine: Line = StringLine
val doubleLine: Line = DoubleLine
fun line(field: Field): Line = FieldLine(field)
infix fun String.lineTo(rhs: Type) = line(this fieldTo rhs)
infix fun String.fieldTo(rhs: Type) = Field(this, rhs)
