package leo14.typed

import leo.base.fold
import leo13.Stack
import leo13.push
import leo13.stack

data class Type(val lineStack: Stack<Line>)

sealed class Line
object NativeLine : Line()
data class ChoiceLine(val choice: Choice) : Line()
data class ArrowLine(val arrow: Arrow) : Line()

data class Choice(val fieldStack: Stack<Field>)
data class Field(val string: String, val rhs: Type)

data class Arrow(val lhs: Type, val rhs: Type)

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
