package leo14

import leo14.typed.compiler.Compiler
import leo14.typed.compiler.parse
import leo14.typed.compiler.reflectScriptLine
import leo14.typed.evaluator.Evaluator
import leo14.typed.evaluator.evaluate
import leo14.typed.evaluator.reflectScriptLine

sealed class TokenReader

data class CompilerTokenReader(val compiler: Compiler<*>) : TokenReader()
data class EvaluatorTokenReader(val evaluator: Evaluator<*>) : TokenReader()

val <T> Compiler<T>.tokenReader: TokenReader get() = CompilerTokenReader(this)
val <T> Evaluator<T>.tokenReader: TokenReader get() = EvaluatorTokenReader(this)

fun TokenReader.read(token: Token): TokenReader =
	when (this) {
		is CompilerTokenReader -> compiler.parse(token).tokenReader
		is EvaluatorTokenReader -> evaluator.evaluate(token).tokenReader
	}

fun TokenReader.read(script: Script): TokenReader =
	when (script) {
		is UnitScript -> this
		is LinkScript -> read(script.link)
	}

fun TokenReader.read(link: ScriptLink): TokenReader =
	read(link.lhs).read(link.line)

fun TokenReader.read(line: ScriptLine): TokenReader =
	when (line) {
		is LiteralScriptLine -> read(token(line.literal))
		is FieldScriptLine -> read(line.field)
	}

fun TokenReader.read(field: ScriptField): TokenReader =
	this
		.read(token(begin(field.string)))
		.read(field.rhs)
		.read(token(end))

val TokenReader.reflectScriptLine: ScriptLine
	get() =
		"token" lineTo script(
			"reader" lineTo script(
				when (this) {
					is CompilerTokenReader -> compiler.reflectScriptLine
					is EvaluatorTokenReader -> evaluator.reflectScriptLine
				}
			)
		)