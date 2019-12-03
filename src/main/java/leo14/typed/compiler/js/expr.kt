package leo14.typed.compiler.js

import leo.base.notNullIf
import leo14.js.ast.*
import leo14.lambda.*
import leo14.lambda.js.expr
import leo14.typed.*

val Typed<Expr>.resolve: Typed<Expr>?
	get() =
		null
			?: resolveShow
			?: resolveOpen
			?: resolveInvoke
			?: resolveJavascript
			?: resolveBinaryOp(numberType, "plus", "+")
			?: resolveBinaryOp(numberType, "minus", "-")
			?: resolveBinaryOp(numberType, "times", "*")
			?: resolveBinaryOp(textType, "plus", "+")
			?: resolveCircle
			?: when (type) {
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
	resolveLinkOrNull?.run {
		notNullIf(head.resolveFieldOrNull?.field == "show" fieldTo type()) {
			tail.term.expr.show.run { typed<Expr>() }
		}
	}

val Typed<Expr>.resolveOpen: Typed<Expr>? get() =
	resolveLinkOrNull?.run {
		notNullIf(head.resolveFieldOrNull?.field == "open" fieldTo type()) {
			tail.term.expr.open.run { typed<Expr>() }
		}
	}

val Typed<Expr>.resolveInvoke: Typed<Expr>? get() =
	resolveLinkOrNull?.run {
		notNullIf(head.resolveFieldOrNull?.field?.string == "invoke") {
			term(tail.term.expr.invoke(term.head.expr)) of expressionType
		}
	}

val Typed<Expr>.resolveJavascript: Typed<Expr>? get() =
	resolveLinkOrNull?.run {
		notNullIf(head.resolveFieldOrNull?.field == "javascript" fieldTo type()) {
			term(expr(id((term.native as StringExpr).string))) of expressionType
		}
	}

fun Typed<Expr>.resolveBinaryOp(type: Type, name: String, op: String): Typed<Expr>? =
		resolveLinkOrNull?.run {
			notNullIf(tail.type == type && head.resolveFieldOrNull?.field == name fieldTo type) {
				term(expr(id("a=>b=>a${op}b")))
					.invoke(tail.term)
					.invoke(head.term) of type
			}
		}

val Typed<Expr>.resolveCircle: Typed<Expr>?
	get() =
		resolveLinkOrNull?.run {
			notNullIf(tail.type == type() && head.resolveFieldOrNull?.field == "circle" fieldTo type()) {
				term(expr(id(
					"(function() {var div=document.createElement('div');" +
						"div.style.width='100px';" +
						"div.style.height='100px';" +
						"div.style.background='yellow';" +
						"div.style.border='2px solid black';" +
						"div.style.borderRadius='50px';" +
						"document.body.innerHTML='';" +
						"document.body.appendChild(div);" +
						"})()"))) of expressionType
			}
		}

// No need for invoke, since we don't evaluate JS
fun Expr.invoke(term: Value<Expr>): Value<Expr>? = null

val exprEvaluator = evaluator(Expr::invoke)