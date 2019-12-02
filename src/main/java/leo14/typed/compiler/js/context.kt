package leo14.typed.compiler.js

import leo14.Literal
import leo14.defaultLanguage
import leo14.js.ast.Expr
import leo14.script
import leo14.typed.Typed
import leo14.typed.TypedLine
import leo14.typed.compiler.Context

val emptyContext: Context<Expr> =
	Context(
		defaultLanguage,
		Typed<Expr>::resolve,
		Literal::typedLine,
		exprEvaluator,
		jsTypeContext,
		TypedLine<Expr>::decompileLiteral,
		script("javascript"))

