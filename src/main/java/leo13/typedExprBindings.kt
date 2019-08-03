package leo13

import leo9.*

data class TypedExprBindings(val stack: Stack<TypedExpr>)

val Stack<TypedExpr>.typedExprBindings get() = TypedExprBindings(this)
fun TypedExprBindings.push(type: TypedExpr) = stack.push(type).typedExprBindings
fun typedExprBindings(vararg types: TypedExpr) = stack(*types).typedExprBindings
fun bindings(type: TypedExpr, vararg types: TypedExpr) = nonEmptyStack(type, *types).typedExprBindings

fun TypedExprBindings.typedExprOrNull(argument: Argument): TypedExpr? =
	stack.drop(argument.previousStack)?.linkOrNull?.value
