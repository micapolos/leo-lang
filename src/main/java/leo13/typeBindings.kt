package leo13

import leo9.Stack
import leo9.nonEmptyStack
import leo9.push
import leo9.stack

data class TypeBindings(val stack: Stack<Type>)

val Stack<Type>.typeBindings get() = TypeBindings(this)
fun TypeBindings.push(type: Type) = stack.push(type).typeBindings
fun typeBindings(vararg types: Type) = stack(*types).typeBindings
fun bindings(type: Type, vararg types: Type) = nonEmptyStack(type, *types).typeBindings
