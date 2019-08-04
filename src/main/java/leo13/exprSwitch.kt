package leo13

import leo.base.notNullIf
import leo.base.orNull
import leo9.*

data class ExprSwitch(val exprStack: Stack<Expr>)
data class TypedExprSwitch(val type: Type, val exprStack: Stack<Expr>)
data class TypedExprSwitchBuilder(val switchOrNull: TypedExprSwitch?)

val Stack<Expr>.exprSwitch get() = ExprSwitch(this)
fun switch(typedExpr: TypedExpr) = TypedExprSwitch(typedExpr.type, stack(typedExpr.expr))
fun builder(switch: TypedExprSwitch?) = TypedExprSwitchBuilder(switch)

fun TypedExprSwitchBuilder.plus(typedExpr: TypedExpr): TypedExprSwitchBuilder? =
	if (switchOrNull == null) builder(switch(typedExpr))
	else switchOrNull.plus(typedExpr)?.let(::builder)

fun TypedExprSwitch.plus(expr: Expr) =
	TypedExprSwitch(type, exprStack.push(expr))

fun TypedExprSwitch.plus(typedExpr: TypedExpr): TypedExprSwitch? =
	notNullIf(type == typedExpr.type) {
		plus(typedExpr.expr)
	}

fun switchOrNull(typedExprStack: Stack<TypedExpr>): TypedExprSwitch? =
	builder(null as TypedExprSwitch?)
		.orNull
		.fold(typedExprStack.reverse) { this?.plus(it) }
		?.switchOrNull
