package leo14

import leo14.native.Native
import leo14.typed.compiler.*
import leo14.typed.decompile
import leo14.typed.typed

val String.eval: String
	get() = leoEval

val Script.eval: Script
	get() =
		leo(compiled(typed()), Phase.EVALUATOR)
			.parse(this)
			.run { this as CompiledParserLeo<Native> }
			.compiledParser
			.compiled
			.typed
			.decompile
