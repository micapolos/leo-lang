package leo15.lambda.runtime.type

import leo13.Stack

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

data class TypeScope<out T>(val closureStack: Stack<TypeClosure<T>>)
data class TypeClosure<out T>(val scope: TypeScope<T>, val type: Type<T>)
