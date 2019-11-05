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

data class Arrow(val lhs: Type, val rhs: Type)

data class Choice(val fieldStack: Stack<Field>)

data class Field(val string: String, val rhs: Type)

val emptyType = Type(stack())
fun type(vararg lines: Line) = Type(stack(*lines))
fun Type.plus(line: Line) = Type(lineStack.push(line))
fun Type.plus(field: Field) = plus(line(choice(field)))
fun type(field: Field, vararg fields: Field) = emptyType.plus(field).fold(fields) { plus(it) }

val nativeLine: Line = NativeLine
fun line(choice: Choice): Line = ChoiceLine(choice)
fun line(arrow: Arrow): Line = ArrowLine(arrow)

val nativeType = type(nativeLine)

val emptyChoice = Choice(stack())
fun choice(vararg fields: Field) = Choice(stack(*fields))
fun Choice.plus(field: Field) = Choice(fieldStack.push(field))

infix fun String.fieldTo(type: Type) = Field(this, type)
