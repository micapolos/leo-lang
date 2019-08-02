package leo13

import leo.base.notNullIf
import leo9.Stack
import leo9.push
import leo9.stack

data class TypeArrows(val arrowStack: Stack<TypeArrow>)

val Stack<TypeArrow>.typeArrows get() = TypeArrows(this)
fun TypeArrows.plus(arrow: TypeArrow) = arrowStack.push(arrow).typeArrows
fun typeArrows(vararg arrows: TypeArrow) = stack(*arrows).typeArrows

fun Script.typedExpr(arrows: TypeArrows, parameter: TypeParameter) =
	null
		?: argumentTypedExprOrNull(arrows, parameter)
		?: TODO()

fun Script.argumentTypedExprOrNull(arrows: TypeArrows, parameter: TypeParameter) =
	notNullIf(this == script("given" lineTo script())) {
		expr(op(argument)) of parameter.type
	}

fun Script.accessTypedExprOrNull(arrows: TypeArrows, parameter: TypeParameter) =
	accessOrNull?.let { access ->
		expr(op(access(access.int)))
	}