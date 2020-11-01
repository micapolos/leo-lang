package leo21.type

import leo13.Stack
import leo13.push
import leo13.stack

sealed class Type
data class StructType(val struct: Struct) : Type()
data class ChoiceType(val choice: Choice) : Type()

data class Struct(val lineStack: Stack<Line>)
data class Choice(val lineStack: Stack<Line>)

object LineString
object LineDouble

sealed class Line
data class StringLine(val string: LineString) : Line()
data class DoubleLine(val double: LineDouble) : Line()
data class FieldLine(val field: Field) : Line()
data class Field(val name: String, val rhs: Type)

fun type(struct: Struct): Type = StructType(struct)
fun type(choice: Choice): Type = ChoiceType(choice)

val Stack<Line>.struct get() = Struct(this)
fun struct(vararg lines: Line) = stack(*lines).struct
fun Struct.plus(line: Line) = lineStack.push(line).struct
val Type.struct get() = (this as StructType).struct

val Stack<Line>.choice get() = Choice(this)
fun choice(vararg lines: Line) = stack(*lines).choice
fun Choice.plus(line: Line) = lineStack.push(line).choice
val Type.choice get() = (this as ChoiceType).choice

fun type(vararg lines: Line) = type(struct(*lines))

val stringLine: Line = StringLine(LineString)
val doubleLine: Line = DoubleLine(LineDouble)
fun line(field: Field): Line = FieldLine(field)
infix fun String.lineTo(rhs: Type) = line(this fieldTo rhs)
infix fun String.fieldTo(rhs: Type) = Field(this, rhs)
