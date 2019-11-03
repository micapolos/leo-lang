package leo14.lambda.js.compiler

import leo13.js.ast.Expr
import leo13.js.ast.expr
import leo13.js.compiler.Typed
import leo14.lambda.booleanOrNull
import leo14.lambda.js.Term

val Term.exprOrNull: Expr?
	get() =
		null
			?: booleanOrNull()?.run(::expr)

val Typed.exprOrNull: Expr?
	get() =
		when (type) {
			else -> TODO()
		}