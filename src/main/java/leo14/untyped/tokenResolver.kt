package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.*

data class TokenReader(
	val resolver: Resolver,
	val parentOrNull: TokenResolverParent?)

data class TokenResolverParent(
	val string: String,
	val tokenReader: TokenReader)

fun Resolver.tokenReader() =
	TokenReader(this, null)

fun tokenReader() =
	resolver().tokenReader()

fun TokenReader.append(program: Program) =
	append(program.script)

fun TokenReader.append(script: Script) =
	fold(script.tokenStack.reverse) { append(it)!! }

fun TokenReader.append(token: Token): TokenReader? =
	when (token) {
		is LiteralToken -> append(token.literal)
		is BeginToken -> begin(token.begin.string)
		is EndToken -> end()
	}

fun TokenReader.append(literal: Literal) =
	append(value(literal))

fun TokenReader.begin(string: String) =
	TokenReader(resolver.clear, TokenResolverParent(string, this))

fun TokenReader.end() =
	parentOrNull?.end(resolver.program)

fun TokenReader.append(value: Value): TokenReader =
	TokenReader(resolver.apply(value), parentOrNull)

fun TokenResolverParent.end(program: Program): TokenReader =
	tokenReader.append(string valueTo program)
