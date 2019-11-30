package leo14.typed.evaluator

import leo14.ScriptLine
import leo14.Token
import leo14.lineTo
import leo14.script
import leo14.typed.compiler.*

data class Evaluator<T>(
	val compiler: Compiler<T>) {
	override fun toString() = "$reflectScriptLine"
}

val <T> Compiler<T>.evaluator
	get() =
		Evaluator(this)

fun <T> Evaluator<T>.evaluate(token: Token): Evaluator<T> =
	compiler.parse(token).evaluate.evaluator

val <T> Compiler<T>.evaluate
	get() =
		if (this is CompiledParserCompiler) compiler(compiledParser.evaluate)
		else this

val <T> Evaluator<T>.reflectScriptLine: ScriptLine
	get() =
		"evaluator" lineTo script(compiler.reflectScriptLine)
