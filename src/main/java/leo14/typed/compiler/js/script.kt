package leo14.typed.compiler.js

import leo14.Script
import leo14.lambda.js.expr.Expr
import leo14.typed.Typed
import leo14.typed.compiler.*

val Script.compileTyped: Typed<Expr>
	get() =
		emptyCompiler
			.parse(this)
			.run { this as CompiledParserCompiler }
			.compiledParser
			.apply { if (parent != null) error("not top level") }
			.compiled
			.typedForEnd

val Script.compile: Script
	get() =
		emptyContext.compile(this)

val Script.evaluate: Script
	get() =
		emptyContext.evaluate(this)
