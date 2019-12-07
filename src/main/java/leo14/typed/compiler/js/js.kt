package leo14.typed.compiler.js

import leo14.Script
import leo14.js.ast.open
import leo14.js.ast.show
import leo14.lambda.js.expr.astExpr

val Script.show
	get() =
		compileTyped.expr.astExpr.show

val Script.open
	get() =
		compileTyped.expr.astExpr.open
