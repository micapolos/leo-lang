package leo14.typed.compiler.js

import leo14.Literal
import leo14.js.ast.Expr
import leo14.typed.Typed
import leo14.typed.TypedLine
import leo14.typed.compiler.Context
import leo14.typed.compiler.defaultDictionary

val emptyContext: Context<Expr> =
	Context<Expr>(
		defaultDictionary,
		Typed<Expr>::resolve,
		Literal::typedLine,
		Expr::invoke,
		TypedLine<Expr>::decompileLiteral)

