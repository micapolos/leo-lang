package leo15

import leo14.Script

data class Evaluator(val compiler: Compiler)

val Compiler.evaluator get() = Evaluator(this)
val emptyEvaluator = emptyCompiler.evaluator

fun Evaluator.plus(script: Script): Evaluator =
	compiler.plus(script).evaluate.evaluator

val Evaluator.typed: Typed
	get() =
		compiler.typed

val Evaluator.script: Script
	get() =
		compiler.typed.script(compiler.library.scope)
