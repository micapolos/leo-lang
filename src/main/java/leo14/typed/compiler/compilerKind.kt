package leo14.typed.compiler

import leo14.lineTo
import leo14.script

enum class CompilerKind {
	COMPILER,
	EVALUATOR,
}

val CompilerKind.reflectScriptLine
	get() =
		"kind" lineTo script(name.toLowerCase())