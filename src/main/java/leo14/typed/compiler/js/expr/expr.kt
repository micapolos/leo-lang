package leo14.typed.compiler.js.expr

import leo.base.notNullIf
import leo14.*
import leo14.code.code
import leo14.js.ast.open
import leo14.js.ast.show
import leo14.lambda.NativeTerm
import leo14.lambda.Term
import leo14.lambda.Value
import leo14.lambda.evaluator
import leo14.lambda.js.expr.*
import leo14.typed.*
import leo14.typed.compiler.js.expressionType

val Typed<Expr>.resolve: Typed<Expr>?
	get() =
		null
			?: resolveNative
			?: resolveShow
			?: resolveOpen
			?: resolveOp(numberType, "plus", "+")
			?: resolveOp(numberType, "minus", "-")
			?: resolveOp(numberType, "times", "*")
			?: resolveOp(textType, "plus", "+")

fun Typed<Expr>.resolveOp(type: Type, name: String, op: String): Typed<Expr>? =
	resolveLink { link ->
		notNullIf(link.tail.type == type && link.head.resolveFieldOrNull?.field == name fieldTo type) {
			link.tail.term.op(op, link.head.term).expr.term of type
		}
	}

val Typed<Expr>.resolveNative: Typed<Expr>?
	get() =
		resolveLink { link ->
			if (link.tail.type == textType &&
				term is NativeTerm &&
				term.native is LiteralExpr &&
				term.native.literal is StringLiteral &&
				link.head.line.fieldOrNull == "native" fieldTo type())
				term.native.literal.string.code.expr.term of expressionType
			else null
		}

val Typed<Expr>.resolveShow: Typed<Expr>?
	get() =
		resolveLink { link ->
			notNullIf(link.head.line.fieldOrNull == "show" fieldTo type()) {
				link.tail.term.astExpr.show.run { typed<Expr>() }
			}
		}

val Typed<Expr>.resolveOpen: Typed<Expr>?
	get() =
		resolveLink { link ->
			notNullIf(link.head.line.fieldOrNull == "open" fieldTo type()) {
				link.tail.term.astExpr.open.run { typed<Expr>() }
			}
		}

// No need for invoke, since we don't evaluate JS
fun Expr.invoke(value: Value<Expr>): Value<Expr>? = null

val exprEvaluator = evaluator(Expr::invoke)

val Term<Expr>.termDecompile: ScriptLine
	get() =
		"javascript" lineTo script(literal(code.string))

// No need for decompiling, since we don't evaluate.
val TypedLine<Expr>.decompileLiteral: Literal?
	get() =
		null