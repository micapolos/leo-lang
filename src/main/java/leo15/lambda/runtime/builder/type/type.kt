package leo15.lambda.runtime.builder.type

import leo13.Stack
import leo13.stack

sealed class Type<out T>
data class ValueType<T>(val value: T) : Type<T>()
data class StructType<T>(val struct: Struct<T>) : Type<T>()
data class ChoiceType<T>(val choice: Choice<T>) : Type<T>()
data class ArrowType<T>(val arrow: Arrow<T>) : Type<T>()
data class RecursiveType<T>(val recursive: Recursive<T>) : Type<T>()
data class RecurseType<T>(val recurse: Recurse) : Type<T>()
data class Struct<out T>(val fieldStack: Stack<Field<T>>)
data class Choice<out T>(val fieldStack: Stack<Field<T>>)
data class Field<out T>(val name: String, val rhsType: Type<T>)
data class Arrow<out T>(val givenType: Type<T>, val givesType: Type<T>)
data class Recursive<out T>(val type: Type<T>)
object Recurse

fun <T> type(value: T): Type<T> = ValueType(value)
fun <T> type(struct: Struct<T>): Type<T> = StructType(struct)
fun <T> type(choice: Choice<T>): Type<T> = ChoiceType(choice)
fun <T> type(arrow: Arrow<T>): Type<T> = ArrowType(arrow)
fun <T> type(recursive: Recursive<T>): Type<T> = RecursiveType(recursive)
fun <T> type(recurse: Recurse): Type<T> = RecurseType(recurse)

fun <T> type(vararg fields: Field<T>): Type<T> = StructType(struct(*fields))

fun <T> struct(vararg fields: Field<T>) = Struct(stack(*fields))
fun <T> choice(vararg fields: Field<T>) = Choice(stack(*fields))
infix fun <T> String.fieldTo(type: Type<T>) = Field(this, type)
infix fun <T> Type<T>.arrowTo(type: Type<T>) = Arrow(this, type)
fun <T> recursive(type: Type<T>) = Recursive(type)
val recurse = Recurse

operator fun <T> String.invoke(type: Type<T>) = this fieldTo type
operator fun <T> String.invoke(vararg fields: Field<T>) = this fieldTo type(*fields)
