package leo14

import leo14.native.Native
import leo14.typed.compiler.CompiledParserCompiler
import leo14.typed.compiler.Phase
import leo14.typed.compiler.compiled
import leo14.typed.compiler.natives.leoEval
import leo14.typed.compiler.parse
import leo14.typed.decompile
import leo14.typed.typed

val String.eval: String
	get() = leoEval

val Script.eval: Script
	get() =
		leo14.typed.compiler.natives.compiler(compiled(typed()), Phase.EVALUATOR)
			.parse(this)
			.run { this as CompiledParserCompiler<Native> }
			.compiledParser
			.compiled
			.typed
			.decompile
