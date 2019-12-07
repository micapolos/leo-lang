package leo14.typed.compiler.js

import leo14.Literal
import leo14.defaultLanguage
import leo14.lambda.Term
import leo14.lambda.js.expr.Expr
import leo14.lambda.js.expr.reflectScriptLine
import leo14.script
import leo14.typed.Typed
import leo14.typed.TypedLine
import leo14.typed.compiler.Context
import leo14.typed.compiler.js.expr.decompileLiteral
import leo14.typed.compiler.js.expr.exprEvaluator
import leo14.typed.compiler.js.expr.resolve
import leo14.typed.compiler.js.expr.termDecompile

val emptyContext: Context<Expr> =
	Context(
		defaultLanguage,
		Typed<Expr>::resolve,
		Literal::typedLine,
		exprEvaluator,
		jsTypeContext,
		TypedLine<Expr>::decompileLiteral,
		Term<Expr>::termDecompile,
		Expr::reflectScriptLine,
		script("javascript"))

