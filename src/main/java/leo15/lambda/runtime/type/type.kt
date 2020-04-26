package leo15.lambda.runtime.type

import leo13.Stack
import leo15.lambda.runtime.builder.Term

data class Type<out T>(val patternTerm: Term<Pattern<T>>)
sealed class Pattern<out T>
data class ValuePattern<T>(val value: T) : Pattern<T>()
data class StructPattern<T>(val struct: Struct<T>) : Pattern<T>()
data class Struct<T>(val lineStack: Stack<Line<T>>)
data class Line<T>(val caseStack: Stack<Case<T>>)
sealed class Case<out T>
data class FieldCase<T>(val field: Field<T>) : Case<T>()
data class ArrowCase<T>(val arrow: Arrow<T>) : Case<T>()
data class Field<out T>(val name: String, val rhs: Type<T>)
data class Arrow<out T>(val lhs: Type<T>, val rhs: Type<T>)
