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
			?: resolveNumberPlus
			?: resolveTextPlus
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

val Typed<Expr>.resolveNumberPlus: Typed<Expr>?
	get() =
		resolveLinkOrNull?.run {
			notNullIf(tail.type == numberType && head.resolveFieldOrNull?.field == "plus" fieldTo numberType) {
				term(expr(id("a=>b=>a+b")))
					.invoke(tail.term)
					.invoke(head.term) of numberType
			}
		}

val Typed<Expr>.resolveTextPlus: Typed<Expr>?
	get() =
		resolveLinkOrNull?.run {
			notNullIf(tail.type == textType && head.resolveFieldOrNull?.field == "plus" fieldTo textType) {
				term(expr(id("a=>b=>a+b")))
					.invoke(tail.term)
					.invoke(head.term) of textType
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