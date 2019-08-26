package leo13.type

import leo9.Stack
import leo9.nonEmptyStack
import leo9.push
import leo9.stack

data class FunctionTypes(val stack: Stack<FunctionType>)

val Stack<FunctionType>.functionTypes get() = FunctionTypes(this)
fun FunctionTypes.plus(type: FunctionType) = stack.push(type).functionTypes
fun functionTypes(vararg types: FunctionType) = stack(*types).functionTypes
fun types(type: FunctionType, vararg types: FunctionType) = nonEmptyStack(type, *types).functionTypes
