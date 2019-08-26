package leo13.script

import leo.base.Empty
import leo.base.fold
import leo13.token.Token

sealed class Interpreter {
	override fun toString() = asScript.toString()
	abstract val asScript: Script
}

data class LeoInterpreter(val leo: Leo) : Interpreter() {
	override fun toString() = super.toString()
	override val asScript = script("leo" lineTo leo.asScript)
}

data class ErrorInterpreter(val error: LeoError) : Interpreter() {
	override fun toString() = super.toString()
	override val asScript = script("error" lineTo error.asScript)
}

data class LeoError(val leo: Leo, val string: String) {
	override fun toString() = asScript.toString()
	val asScript
		get() = script(
			"leo" lineTo leo.asScript,
			"string" lineTo script(string lineTo script()))
}

fun interpreter(leo: Leo): Interpreter = LeoInterpreter(leo)
fun interpreter(error: LeoError): Interpreter = ErrorInterpreter(error)
fun Leo.error(string: String) = LeoError(this, string)

val Empty.interpreter get() = leo

fun Interpreter.push(token: Token) =
	when (this) {
		is LeoInterpreter -> leo.push(token)
		is ErrorInterpreter -> this
	}

fun Interpreter.push(script: Script) =
	fold(script.tokenSeq) { push(it) }
