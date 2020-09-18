package leo17

import leo13.Stack

sealed class Type<out T>
data class ValueType<out T>(val value: T) : Type<T>()
data class StructType<out T>(val struct: TypeStruct<T>) : Type<T>()
data class ChoiceType<out T>(val choice: TypeChoice<T>) : Type<T>()
data class FunctionType<out T>(val function: TypeFunction<T>) : Type<T>()
data class RecurseType<out T>(val recurse: TypeRecurse<T>) : Type<T>()
data class RecursiveType<out T>(val recursive: TypeRecursive<T>) : Type<T>()

data class TypeStruct<out T>(val fieldStack: Stack<TypeField<T>>)
data class TypeChoice<out T>(val caseStack: Stack<TypeCase<T>>)

data class TypeField<out T>(val word: String, val rhsType: Type<T>)
data class TypeCase<out T>(val word: String, val rhsType: Type<T>)

data class TypeFunction<out T>(val lhsType: Type<T>, val rhsType: Type<T>)
data class TypeRecurse<out T>(val depth: Int)
data class TypeRecursive<out T>(val type: Type<T>)