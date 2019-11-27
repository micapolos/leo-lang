package leo14.typed.compiler.js

import leo.base.notNullIf
import leo14.js.ast.*
import leo14.lambda.*
import leo14.lambda.js.expr
import leo14.lambda.js.open
import leo14.lambda.js.show
import leo14.type.field
import leo14.typed.*

val Typed<Expr>.resolve: Typed<Expr>?
	get() =
		null
			?: resolveShow
			?: resolveOpen
			?: resolveInvoke
			?: resolveJavascript
			?: when (type) {
				type(numberLine, "plus" lineTo numberType) ->
					decompileLinkOrNull!!.run {
						term(expr(tail.term.expr.op("+", term.head.expr))) of numberType
					}
				type(textLine, "plus" lineTo textType) ->
					decompileLinkOrNull!!.run {
						term(expr(tail.term.expr.op("+", term.head.expr))) of textType
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

val Typed<Expr>.resolveShow: Typed<Expr>? get() =
	decompileLinkOrNull?.run {
		notNullIf(head.typedField.field == "show" fieldTo type()) {
			tail.term.eval.show.run { typed<Expr>() }
		}
	}

val Typed<Expr>.resolveOpen: Typed<Expr>? get() =
	decompileLinkOrNull?.run {
		notNullIf(head.typedField.field == "show" fieldTo type()) {
			tail.term.expr.open.run { typed<Expr>() }
		}
	}

val Typed<Expr>.resolveInvoke: Typed<Expr>? get() =
	decompileLinkOrNull?.run {
		notNullIf(head.typedField.field.string == "invoke") {
			term(tail.term.expr.invoke(term.head.expr)) of expressionType
		}
	}

val Typed<Expr>.resolveJavascript: Typed<Expr>? get() =
	decompileLinkOrNull?.run {
		notNullIf(head.typedField.field == "javascript" fieldTo type()) {
			term(expr(id((term.native as StringExpr).string))) of expressionType
		}
	}

// No need for invoke, since we don't evaluate JS
fun Expr.invoke(term: Term<Expr>): Term<Expr> =
	error("$this.invoke($term)")

