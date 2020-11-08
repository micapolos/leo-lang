package leo21.type.value

import leo13.Stack
import leo13.stack
import leo21.type.Type

data class TypeContext(val typeStack: Stack<Type>)

val emptyTypeContext = TypeContext(stack())