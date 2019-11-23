package leo14.typed.compiler

import leo14.Literal
import leo14.Script
import leo14.js.ast.Expr
import leo14.js.ast.expr
import leo14.js.ast.invoke
import leo14.js.ast.op
import leo14.lambda.Term
import leo14.lambda.js.expr
import leo14.lambda.js.literalOrNull
import leo14.lambda.js.show
import leo14.lambda.native
import leo14.lambda.term
import leo14.typed.*

val jsContext: Context<Expr> =
	Context<Expr>(
		defaultDictionary,
		Typed<Expr>::jsResolve,
		Literal::jsTypedLine,
		Expr::invoke,
		TypedLine<Expr>::jsDecompileLiteral)

val Typed<Expr>.jsResolve: Typed<Expr>?
	get() =
		when (type) {
			type(nativeLine, "plus" lineTo nativeType) ->
				apply { println(this) }.lineLink.run { term(expr(tail.term.native.op("+", head.term.native))) of nativeType }
			else -> null
		}

fun Expr.invoke(term: Term<Expr>): Term<Expr> =
	term(invoke(term.native))

val Literal.jsTypedLine: TypedLine<Expr>
	get() =
		term(expr) of nativeLine

val TypedLine<Expr>.jsDecompileLiteral: Literal?
	get() =
		if (line is NativeLine) term.native.literalOrNull
		else null

val Script.jsShow
	get() =
		compiler(
			CompiledParser(
				null,
				jsContext,
				Phase.COMPILER,
				Compiled(memory(), typed())))
			.parse(this)
			.run { this as CompiledParserCompiler }
			.compiledParser
			.compiled
			.resolveForEnd
			.typed
			.term
			.show
