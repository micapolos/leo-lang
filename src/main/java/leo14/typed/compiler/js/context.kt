package leo14.typed.compiler.js

import leo14.Literal
import leo14.defaultLanguage
import leo14.lambda.Term
import leo14.lambda.js.expr.Expr
import leo14.lambda.js.expr.reflectScriptLine
import leo14.script
import leo14.typed.TypedLine
import leo14.typed.compiler.Compiled
import leo14.typed.compiler.Context

val emptyContext: Context<Expr> =
	Context(
		defaultLanguage,
		Compiled<Expr>::resolve,
		Literal::typedLine,
		exprEvaluator,
		jsTypeContext,
		TypedLine<Expr>::decompileLiteral,
		Term<Expr>::termDecompile,
		Expr::reflectScriptLine,
		script("javascript"))

