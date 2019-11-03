package leo14.lambda.js

import leo13.js.ast.open
import leo13.js.ast.show
import leo14.*
import leo14.lambda.valueCompiler

val Value.open get() = expr.open
val Value.show get() = expr.show

val Script.show
	get() =
		valueCompiler(
			compileError("fallback"),
			compileExpr,
			ret())
			.write(this)
			.write(token(end))
			.result<Value>()
			.show
