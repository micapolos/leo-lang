package leo15.lambda.runtime.type

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

val <T> T.type: Type<T> get() = ValueType(this)
val <T> Struct<T>.type: Type<T> get() = StructType(this)
val <T> Choice<T>.type: Type<T> get() = ChoiceType(this)
val <T> Arrow<T>.type: Type<T> get() = ArrowType(this)
fun <T> Recursive<T>.type(): Type<T> = RecursiveType(this)
fun <T> Recurse.type(): Type<T> = RecurseType(this)

fun <T> struct(vararg fields: Field<T>) = Struct(stack(*fields))
fun <T> choice(vararg fields: Field<T>) = Choice(stack(*fields))
infix fun <T> String.fieldTo(type: Type<T>) = Field(this, type)
infix fun <T> Type<T>.arrowTo(type: Type<T>) = Arrow(this, type)
val <T> Type<T>.recursive get() = Recursive(this)
val recurse = Recurse
