package leo13

import leo.base.notNullIf
import leo9.Stack
import leo9.nonEmptyStack
import leo9.push
import leo9.stack

data class FunctionTypes(val stack: Stack<FunctionType>)

val Stack<FunctionType>.functionTypes get() = FunctionTypes(this)
fun FunctionTypes.plus(type: FunctionType) = stack.push(type).functionTypes
fun functionTypes(vararg types: FunctionType) = stack(*types).functionTypes
fun types(type: FunctionType, vararg types: FunctionType) = nonEmptyStack(type, *types).functionTypes

fun Script.typedExpr(arrows: FunctionTypes, parameter: TypeParameter) =
	null
		?: argumentTypedExprOrNull(arrows, parameter)
		?: TODO()

fun Script.argumentTypedExprOrNull(arrows: FunctionTypes, parameter: TypeParameter) =
	notNullIf(this == script("given" lineTo script())) {
		expr(op(argument())) of parameter.type
	}

fun Script.accessTypedExprOrNull(arrows: FunctionTypes, parameter: TypeParameter) =
	accessOrNull?.let { access ->
		expr(op(access(access.int)))
	}