package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.*

data class Tokenizer(
	val interpreter: Interpreter,
	val parentOrNull: TokenizerParent?)

data class TokenizerParent(
	val string: String,
	val tokenizer: Tokenizer)

fun Interpreter.tokenizer() =
	Tokenizer(this, null)

fun tokenizer() =
	interpreter().tokenizer()

fun Tokenizer.plus(script: Script) =
	fold(script.tokenStack.reverse) { plus(it)!! }

fun Tokenizer.plus(token: Token): Tokenizer? =
	when (token) {
		is LiteralToken -> plus(token.literal)
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end()
	}

fun Tokenizer.plus(literal: Literal) =
	plus(line(literal))

fun Tokenizer.begin(string: String) =
	Tokenizer(interpreter.clear, TokenizerParent(string, this))

fun Tokenizer.end() =
	parentOrNull?.end(interpreter.script)

fun Tokenizer.plus(scriptLine: ScriptLine): Tokenizer =
	Tokenizer(interpreter.interpret(scriptLine), parentOrNull)

fun TokenizerParent.end(script: Script): Tokenizer =
	tokenizer.plus(string lineTo script)
