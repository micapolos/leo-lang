package leo16.lambda.type

import leo13.Stack
import leo13.push

data class TypeScope(val typeStack: Stack<Type>)

val Stack<Type>.scope get() = TypeScope(this)
operator fun TypeScope.plus(type: Type) = typeStack.push(type).scope
