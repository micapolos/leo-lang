package leo14.typed.compiler.js

import leo14.Literal
import leo14.defaultDictionary
import leo14.js.ast.Expr
import leo14.typed.Typed
import leo14.typed.TypedLine
import leo14.typed.compiler.Context

val emptyContext: Context<Expr> =
	Context(
		defaultDictionary,
		Typed<Expr>::resolve,
		Literal::typedLine,
		exprEvaluator,
		TypedLine<Expr>::decompileLiteral)

