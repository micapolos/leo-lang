package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.*

data class Tokenizer(
	val resolver: Resolver,
	val parentOrNull: TokenizerParent?)

data class TokenizerParent(
	val string: String,
	val tokenizer: Tokenizer)

fun Resolver.tokenizer() =
	Tokenizer(this, null)

fun tokenizer() =
	resolver().tokenizer()

fun Tokenizer.append(program: Program) =
	append(program.script)

fun Tokenizer.append(script: Script) =
	fold(script.tokenStack.reverse) { append(it)!! }

fun Tokenizer.append(token: Token): Tokenizer? =
	when (token) {
		is LiteralToken -> append(token.literal)
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end()
	}

fun Tokenizer.append(literal: Literal) =
	append(value(literal))

fun Tokenizer.begin(string: String) =
	Tokenizer(resolver.clear, TokenizerParent(string, this))

fun Tokenizer.end() =
	parentOrNull?.end(resolver.program)

fun Tokenizer.append(value: Value): Tokenizer =
	Tokenizer(resolver.apply(value), parentOrNull)

fun TokenizerParent.end(program: Program): Tokenizer =
	tokenizer.append(string valueTo program)
