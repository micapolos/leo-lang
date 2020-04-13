package leo14.untyped.typed.lambda

import leo14.Script
import leo14.script

val Script.compile: Script
	get() =
		script(emptyCompiler.plus(this).compiled.reflectScriptLine)

val Script.eval: Script
	get() =
		emptyEvaluator.plus(this).script