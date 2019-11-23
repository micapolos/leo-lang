package leo14.typed.compiler.js

import leo14.Script
import leo14.lambda.js.show
import leo14.typed.compiler.*
import leo14.typed.typed

val Script.show
	get() =
		compiler(
			CompiledParser(
				null,
				emptyContext,
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
