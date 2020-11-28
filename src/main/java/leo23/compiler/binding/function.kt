package leo23.compiler.binding

import leo13.Stack
import leo23.type.Type

data class BindingFunction(val typeStack: Stack<Type>, val doesType: Type)