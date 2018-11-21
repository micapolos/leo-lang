package leo

import leo.base.*

data class Function(
	val bodyBinaryTrie: BinaryTrie<Body>)

val BinaryTrie<Body>.function
	get() =
		Function(this)

val emptyFunction: Function =
	emptyBinaryTrie<Body>().function

fun Function.get(bit: Bit): Match? =
	bodyBinaryTrie.get(bit)?.match

fun Function.set(bit: Bit, matchOrNull: Match?): Function =
	bodyBinaryTrie.set(bit, matchOrNull?.bodyBinaryTrieMatch).function

fun Function.get(bitStream: Stream<Bit>): Match? =
	bodyBinaryTrie.get(bitStream)?.match

fun Function.get(term: Term<Nothing>): Match? =
	get(term.bitStream)

// == invoke & apply

fun Function.invoke(argument: Term<Nothing>): Term<Nothing>? =
	invoke(argument) { argument.invokeFallback }

fun Function.invoke(argument: Term<Nothing>, fallbackFn: () -> Term<Nothing>?): Term<Nothing>? =
	apply(argument).let { theTermOrNull ->
		if (theTermOrNull == null) fallbackFn()
		else theTermOrNull.value
	}

fun Function.apply(argument: Term<Nothing>): The<Term<Nothing>?>? =
	get(argument)?.bodyOrNull.let { bodyOrNull ->
		if (bodyOrNull == null) null
		else bodyOrNull.apply(argument).the
	}

// === fallback

val Term<Nothing>.invokeFallback: Term<Nothing>?
	get() = invokeExtern.let { theTermOrNull ->
		if (theTermOrNull == null) invokeSelect
		else theTermOrNull.value
	}


val Term<Nothing>.invokeSelect: Term<Nothing>
	get() =
		when (this) {
			is Term.Meta -> this
			is Term.Structure ->
				when {
					rhsTermOrNull != null -> this
					lhsTermOrNull == null -> this
					else -> lhsTermOrNull.select(word)?.value ?: word.fieldTo(lhsTermOrNull).term
				}
		}

// === define

fun Function.define(rule: Rule): Function? =
	define(rule.patternTerm, rule.body)

fun Function.define(patternTerm: Term<Pattern>, body: Body): Function? =
	define(patternTerm) { body.match }

fun Function.define(patternTerm: Term<Pattern>, defineNext: Function.() -> Match?): Function? =
	defineToken(patternTerm.tokenStream, defineNext)

fun Function.define(pattern: Pattern, defineNext: Function.() -> Match?): Function? =
	orNull.fold(pattern.patternTermStack.reverse.stream) { patternTerm ->
		this?.define(patternTerm, defineNext)
	}

fun Function.defineToken(tokenStream: Stream<Token<Pattern>>, defineNext: Function.() -> Match?): Function? =
	define(tokenStream.first) {
		tokenStream.nextOrNull.let { nextTokenStreamOrNull ->
			if (nextTokenStreamOrNull == null) defineNext()
			else defineToken(nextTokenStreamOrNull, defineNext)?.match
		}
	}

fun Function.define(token: Token<Pattern>, defineNext: Function.() -> Match?): Function? =
	when (token) {
		is MetaEndToken -> define(token, defineNext)
		is BeginToken -> define(token, defineNext)
		is EndToken -> defineEndToken(defineNext)
	}

fun Function.define(token: MetaEndToken<Pattern>, defineNext: Function.() -> Match?): Function? =
	define(token.value, defineNext)

fun Function.define(token: BeginToken<Pattern>, defineNext: Function.() -> Match?): Function? =
	defineBit(token.word.bitStream.then { beginByte.bitStream }, defineNext)

fun Function.defineEndToken(defineNext: Function.() -> Match?): Function? =
	define(endByte, defineNext)

fun Function.define(word: Word, defineNext: Function.() -> Match?): Function? =
	defineBit(word.letterStream.map(Letter::byte).map(Byte::bitStream).join, defineNext)

fun Function.define(byte: Byte, defineNext: Function.() -> Match?): Function? =
	defineBit(byte.bitStream, defineNext)

fun Function.defineBit(bitStream: Stream<Bit>, defineNext: Function.() -> Match?): Function? =
	bodyBinaryTrie.defineBit(bitStream) {
		defineNext.invoke(function)?.bodyBinaryTrieMatch
	}?.function

fun Function.define(bit: Bit, defineNext: Function.() -> Match?): Function? =
	bodyBinaryTrie.define(bit) {
		defineNext.invoke(function)?.bodyBinaryTrieMatch
	}?.function