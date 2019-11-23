package leo14.typed.compiler.js

import leo14.js.ast.Expr
import leo14.js.ast.invoke
import leo14.js.dsl.alert
import leo14.js.dsl.window
import leo14.lambda.Term
import leo14.lambda.js.expr
import leo14.lambda.term
import leo14.typed.*

val Typed<Expr>.resolve: Typed<Expr>?
	get() =
		when (type) {
			type(
				"javascript" lineTo type(
					"alert" lineTo type(
						"string" lineTo nativeType))) ->
				term(window.alert.invoke(term.expr)) of type()
			else -> null
		}

// No need for invoke, since we don't evaluate JS
fun Expr.invoke(term: Term<Expr>): Term<Expr> =
	error("$this.invoke($term)")

