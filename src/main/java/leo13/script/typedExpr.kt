package leo13.script

import leo.base.notNullIf
import leo13.*
import leo9.mapFirst
import leo9.mapOnly

data class TypedExpr(val expr: Expr, val type: Type)
data class TypedExprLine(val name: String, val rhs: TypedExpr)

infix fun Expr.of(type: Type) = TypedExpr(this, type)
infix fun String.lineTo(typedExpr: TypedExpr) = TypedExprLine(this, typedExpr)
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
		castTypedScript.expr.eval(bindings()) of castTypedScript.type
	}

fun TypedExpr.accessOrNull(name: String): TypedExpr? =
	type
		.onlyLineOrNull
		?.rhs
		?.choiceStack
		?.mapOnly {
			onlyLineOrNull?.let { line ->
				notNullIf(line.name == name) {
					expr.plus(op(name)) of type(line)
				}
			}
		}

val Script.exactTypedExpr
	get() =
		expr of exactType
