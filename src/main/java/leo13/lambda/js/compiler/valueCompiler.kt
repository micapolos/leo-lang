package leo13.lambda.js.compiler

import leo13.js.ast.Expr
import leo13.js.ast.expr
import leo13.js.compiler.Typed
import leo13.lambda.booleanOrNull
import leo13.lambda.js.Value

val Value.exprOrNull: Expr?
	get() =
		null
			?: booleanOrNull()?.run(::expr)

val Typed.exprOrNull: Expr?
	get() =
		when (type) {
			else -> TODO()
		}