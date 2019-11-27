package leo14.typed.compiler.js

import leo14.js.ast.*
import leo14.lambda.Term
import leo14.lambda.head
import leo14.lambda.js.expr
import leo14.lambda.native
import leo14.lambda.term
import leo14.typed.*

val Typed<Expr>.resolve: Typed<Expr>?
	get() =
		when (type) {
			type(expressionLine, "open" lineTo type()) ->
				decompileLinkOrNull!!.tail.apply { expr.open }.run { typed<Expr>() }
			type(expressionLine, "show" lineTo type()) ->
				decompileLinkOrNull!!.tail.apply { expr.show }.run { typed<Expr>() }
			javascriptType(textLine) -> term of expressionType
			javascriptType(numberLine) -> term of expressionType
			javascriptType(expressionType) ->
				term(expr(id((term.native as StringExpr).string))) of expressionType
			type(expressionLine, "invoke" lineTo expressionType) ->
				decompileLinkOrNull!!.run {
					term(tail.term.expr.invoke(term.head.expr)) of expressionType
				}
			type(expressionLine, "plus" lineTo expressionType) ->
				decompileLinkOrNull!!.run {
					term(expr(tail.term.expr.op("+", term.head.expr))) of expressionType
				}
			type(
				expressionLine,
				"set" lineTo textType,
				"to" lineTo expressionType) ->
				decompileLinkOrNull!!.let { firstLink ->
					firstLink.tail.decompileLinkOrNull!!.let { secondLink ->
						term(expr(fn(args("it"),
							block(expr(id("it"))
								.set(
									(secondLink.head.term.expr as StringExpr).string,
									firstLink.head.term.expr),
								stmt(ret(expr(id("it")))))))
							.invoke(secondLink.tail.term.expr)) of expressionType
					}
				}
			else -> null
		}

// No need for invoke, since we don't evaluate JS
fun Expr.invoke(term: Term<Expr>): Term<Expr> =
	error("$this.invoke($term)")

