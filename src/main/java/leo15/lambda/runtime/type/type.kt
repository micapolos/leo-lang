package leo15.lambda.runtime.type

import leo13.Stack

sealed class Type<out T>
data class ValueType<T>(val value: T) : Type<T>()
data class StructType<T>(val struct: Struct<T>) : Type<T>()
data class ChoiceType<T>(val choice: Choice<T>) : Type<T>()
data class ArrowType<T>(val arrow: Arrow<T>) : Type<T>()
data class BindType<T>(val bind: Bind<T>) : Type<T>()
data class IndexType<T>(val index: Int) : Type<T>()
data class Struct<T>(val fieldStack: Stack<Field<T>>)
data class Choice<T>(val fieldStack: Stack<Field<T>>)
data class Field<T>(val name: String, val rhs: Type<T>)
data class Arrow<out T>(val lhs: Type<T>, val rhs: Type<T>)
data class Bind<out T>(val type: Type<T>)