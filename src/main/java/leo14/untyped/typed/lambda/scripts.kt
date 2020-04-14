package leo14.untyped.typed.lambda

import leo14.Script
import leo14.script

val Script.compiler: Compiler
	get() =
		emptyCompiler.plus(this)

val Script.compiled: Compiled
	get() =
		compiler.compiled

val Script.compile: Script
	get() =
		script(compiled.reflectScriptLine)

val Script.eval: Script
	get() =
		emptyEvaluator.plus(this).script