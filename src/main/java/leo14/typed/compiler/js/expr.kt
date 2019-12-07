package leo14.typed.compiler.js

import leo.base.notNullIf
import leo14.ScriptLine
import leo14.js.ast.*
import leo14.lambda.*
import leo14.lambda.js.code
import leo14.lambda.js.expr
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.typed.*

val Typed<Expr>.resolve: Typed<Expr>?
	get() =
		null
			?: resolveShow
			?: resolveOpen
			?: resolveInvoke
			?: resolveEval
			?: resolveBinaryOpNew(numberType, "plus", "+")
			?: resolveBinaryOpNew(numberType, "minus", "-")
			?: resolveBinaryOpNew(numberType, "times", "*")
			?: resolveBinaryOpNew(textType, "plus", "+")
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

val Typed<Expr>.resolveEval: Typed<Expr>?
	get() =
	resolveLinkOrNull?.run {
		notNullIf(head.resolveFieldOrNull?.field == "eval" fieldTo type()) {
			term(expr(id((term.native as StringExpr).string))) of expressionType
		}
	}

fun Typed<Expr>.resolveBinaryOpNew(type: Type, name: String, op: String): Typed<Expr>? =
	resolveLink { link ->
		notNullIf(link.tail.type == type && link.head.resolveFieldOrNull?.field == name fieldTo type) {
			term(expr(id("a=>b=>a${op}b")))
				.invoke(link.tail.term)
				.invoke(link.head.term) of type
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
fun Expr.invoke(value: Value<Expr>): Value<Expr>? = null

val exprEvaluator = evaluator(Expr::invoke)

val Term<Expr>.termDecompile: ScriptLine
	get() =
		"javascript" lineTo script(literal(code))