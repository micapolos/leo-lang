package leo14.untyped

import leo13.fold
import leo13.reverse
import leo14.*

enum class TokenReaderMode { RESOLVE, META }

data class TokenReader(
	val resolver: Resolver,
	val parentOrNull: TokenReaderParent?,
	val mode: TokenReaderMode)

data class TokenReaderParent(
	val string: String,
	val tokenReader: TokenReader)

fun Resolver.tokenReader() =
	TokenReader(this, null, TokenReaderMode.RESOLVE)

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

fun TokenReader.begin(name: String) =
	TokenReader(
		resolver.clear,
		TokenReaderParent(name, this),
		mode.begin(name))

fun TokenReaderMode.begin(name: String) =
	when (this) {
		TokenReaderMode.RESOLVE ->
			if (name == "meta") TokenReaderMode.META
			else TokenReaderMode.RESOLVE
		TokenReaderMode.META ->
			TokenReaderMode.RESOLVE
	}

fun TokenReaderMode.begin(resolver: Resolver, parentOrNull: TokenReaderParent?, name: String) =
	when (this) {
		TokenReaderMode.RESOLVE ->
			if (name == "meta") TokenReader(resolver, parentOrNull, TokenReaderMode.META)
			else TokenReader(resolver.clear, parentOrNull, TokenReaderMode.META)
		TokenReaderMode.META ->
			TokenReader(resolver, parentOrNull, TokenReaderMode.RESOLVE)
	}

fun TokenReader.end() =
	parentOrNull?.end(resolver.program, mode)

fun TokenReader.updateResolver(fn: Resolver.() -> Resolver) =
	copy(resolver = resolver.fn())

fun TokenReader.append(value: Value) =
	updateResolver { mode.append(resolver, value) }

fun TokenReaderMode.append(resolver: Resolver, value: Value) =
	when (this) {
		TokenReaderMode.RESOLVE -> resolver.apply(value)
		TokenReaderMode.META -> resolver.append(value)
	}

fun TokenReaderParent.end(program: Program, fromMode: TokenReaderMode): TokenReader =
	when (fromMode) {
		TokenReaderMode.RESOLVE -> tokenReader.append(string valueTo program)
		TokenReaderMode.META -> tokenReader.updateResolver { append(program) }
	}
