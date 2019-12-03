package leo14

import leo14.native.Native
import leo14.typed.compiler.CompiledParserCompiler
import leo14.typed.compiler.natives.emptyEvaluator
import leo14.typed.compiler.natives.leoEval
import leo14.typed.compiler.parse
import leo14.typed.decompile

val String.eval: String
	get() = leoEval

val Script.eval: Script
	get() =
		emptyEvaluator
			.parse(this)
			.run { this as CompiledParserCompiler<Native> }
			.compiledParser
			.compiled
			.typed
			.decompile
