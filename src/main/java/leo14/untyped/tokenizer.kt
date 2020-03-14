package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.*

data class Tokenizer(
	val liner: Liner,
	val parentOrNull: TokenizerParent?)

data class TokenizerParent(
	val string: String,
	val tokenizer: Tokenizer)

fun Liner.tokenizer() =
	Tokenizer(this, null)

fun tokenizer() =
	liner().tokenizer()

fun Tokenizer.append(script: Script) =
	fold(script.tokenStack.reverse) { append(it)!! }

fun Tokenizer.append(token: Token): Tokenizer? =
	when (token) {
		is LiteralToken -> append(token.literal)
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end()
	}

fun Tokenizer.append(literal: Literal) =
	append(line(literal))

fun Tokenizer.begin(string: String) =
	Tokenizer(liner.clear, TokenizerParent(string, this))

fun Tokenizer.end() =
	parentOrNull?.end(liner.script)

fun Tokenizer.append(scriptLine: ScriptLine): Tokenizer =
	Tokenizer(liner.append(scriptLine), parentOrNull)

fun TokenizerParent.end(script: Script): Tokenizer =
	tokenizer.append(string lineTo script)
