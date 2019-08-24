package leo13.script

import leo.base.ifOrNull
import leo.base.notNullIf
import leo13.*
import leo13.script.evaluator.evaluate
import leo9.mapFirst
import leo9.mapOnly

data class TypedExpr(val expr: Expr, val type: Type)
data class TypedExprLine(val name: String, val rhs: TypedExpr)
data class TypedExprLink(val lhs: TypedExpr, val line: TypedExprLine)

infix fun Expr.of(type: Type) = TypedExpr(this, type)
infix fun String.lineTo(typedExpr: TypedExpr) = TypedExprLine(this, typedExpr)
fun TypedExpr.linkTo(line: TypedExprLine) = TypedExprLink(this, line)
fun TypedExpr.plus(line: TypedExprLine) = expr.plus(op(line.name lineTo line.rhs.expr)) of type.plus(line.name lineTo line.rhs.type)

fun Type.castOrNull(typedExpr: TypedExpr): Expr? =
	notNullIf(contains(typedExpr.type)) {
		typedExpr.expr
	}

fun Types.cast(typedExpr: TypedExpr): TypedExpr =
	typeStack.mapFirst {
		castOrNull(typedExpr)?.of(this)
	} ?: typedExpr

fun Types.cast(typedScript: TypedScript): TypedScript =
	cast(typedScript.script.expr of typedScript.type).let { castTypedScript ->
		castTypedScript.expr.evaluate(leo13.script.evaluator.bindings()) of castTypedScript.type
	}

fun TypedExpr.accessOrNull(name: String): TypedExpr? =
	type
		.onlyLineOrNull
		?.rhs
		?.lineStack
		?.mapOnly {
			notNullIf(this.name == name) {
				expr.plus(op(get(name))) of type(this)
			}
		}

val Script.exactTypedExpr
	get() =
		expr of exactType

val TypedExprLink.ofTypedExprOrNull
	get() =
		ifOrNull(line.name == "of") {
			line.rhs.type.scriptOrNull?.type?.let { type ->
				type.castOrNull(lhs)?.of(type)
			}
		}
