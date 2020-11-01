package leo21.type

import leo.base.fold
import leo13.Stack
import leo13.any
import leo13.push
import leo13.stack

sealed class Type
object StringType : Type()
object DoubleType : Type()
data class StructType(val struct: Struct) : Type()
data class ChoiceType(val choice: Choice) : Type()
data class ArrowType(val arrow: Arrow) : Type()

data class Struct(val fieldStack: Stack<Field>)
data class Choice(val fieldStack: Stack<Field>)
data class Arrow(val lhs: Type, val rhs: Type)

data class Field(val name: String, val rhs: Type)

fun type(struct: Struct): Type = StructType(struct)
fun type(choice: Choice): Type = ChoiceType(choice)
fun type(arrow: Arrow): Type = ArrowType(arrow)

val Type.struct get() = (this as StructType).struct
val Type.choice get() = (this as ChoiceType).choice
val Type.arrow get() = (this as ArrowType).arrow

val emptyStruct = Struct(stack())
fun struct(vararg lines: Field) = emptyStruct.fold(lines) { plus(it) }
fun Struct.plus(line: Field) =
	if (fieldStack.any { name == line.name }) error("duplicate field")
	else Struct(fieldStack.push(line))

val emptyChoice = Choice(stack())
fun choice(vararg lines: Field) = emptyChoice.fold(lines) { plus(it) }
fun Choice.plus(field: Field) =
	if (fieldStack.any { name == field.name }) error("duplicate case")
	else Choice(fieldStack.push(field))

fun type(vararg lines: Field) = type(struct(*lines))

val stringType: Type = StringType
val doubleType: Type = DoubleType
infix fun String.fieldTo(rhs: Type) = Field(this, rhs)

infix fun Type.arrowTo(rhs: Type) = Arrow(this, rhs)