package leo14.typed.compiler.js

import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.js.ast.op
import leo14.lambda.Term
import leo14.lambda.native
import leo14.lambda.term
import leo14.typed.*

val Typed<Expr>.resolve: Typed<Expr>?
	get() =
		when (type) {
			type(nativeLine, "plus" lineTo nativeType) ->
				apply { println(this) }.lineLink.run { term(expr(tail.term.native.op("+", head.term.native))) of nativeType }
			else -> null
		}

// No need for invoke, since we don't evaluate JS
fun Expr.invoke(term: Term<Expr>): Term<Expr> =
	error("$this.invoke($term)")

