package leo13.lambda.js

import leo13.js.ast.open
import leo13.js.ast.show
import leo13.js.compiler.*
import leo13.lambda.valueCompiler
import leo13.script.v2.Script

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
