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

fun Function.invoke(argument: Term<Nothing>): Term<Nothing> =
	invoke(argument) { argument.invokeFallback }

fun Function.invoke(argument: Term<Nothing>, fallbackFn: () -> Term<Nothing>): Term<Nothing> =
	apply(argument).let { termOrNull ->
		if (termOrNull == null) fallbackFn()
		else termOrNull
	}

fun Function.apply(argument: Term<Nothing>): Term<Nothing>? =
	get(argument)?.bodyOrNull.let { bodyOrNull ->
		bodyOrNull?.apply(argument)
	}

// === fallback

val Term<Nothing>.invokeFallback: Term<Nothing>
	get() = invokeExtern.let { termOrNull ->
		if (termOrNull == null) evaluateSelect
		else termOrNull
	}

// === define

fun Function.define(rule: Rule): Function? =
	define(rule.patternTerm, rule.body)

fun Function.define(patternTerm: Term<Pattern>, body: Body): Function? =
	define(patternTerm) { body.match }

fun Function.define(patternTerm: Term<Pattern>, defineNext: Function.() -> Match?): Function? =
	defineToken(patternTerm.tokenStream, defineNext)

fun Function.define(pattern: Pattern, defineNext: Function.() -> Match?): Function? =
	when (pattern) {
		is OneOfPattern -> define(pattern, defineNext)
		is RecursionPattern -> define(pattern, defineNext)
	}

fun Function.define(pattern: OneOfPattern, defineNext: Function.() -> Match?): Function? =
	orNull.fold(pattern.patternTermStream) { patternTerm ->
		this?.define(patternTerm, defineNext)
	}

fun Function.define(pattern: RecursionPattern, defineNext: Function.() -> Match?): Function? =
	TODO()

fun Function.defineToken(tokenStream: Stream<Token<Pattern>>, defineNext: Function.() -> Match?): Function? =
	orNull.read(tokenStream) { token, nextOrNull ->
		define(token) {
			if (nextOrNull == null) defineNext()
			else defineToken(nextOrNull, defineNext)?.match
		}
	}

fun Function.define(token: Token<Pattern>, defineNext: Function.() -> Match?): Function? =
	when (token) {
		is MetaToken -> define(token, defineNext)
		is WordToken -> define(token, defineNext)
		is ControlToken -> when (token.control) {
			is BeginControl -> defineBeginToken(defineNext)
			is EndControl -> defineEndToken(defineNext)
		}
	}

fun Function.define(token: MetaToken<Pattern>, defineNext: Function.() -> Match?): Function? =
	define(token.meta, defineNext)

fun Function.define(token: WordToken<Pattern>, defineNext: Function.() -> Match?): Function? =
	define(token.word, defineNext)

fun Function.define(meta: Meta<Pattern>, defineNext: Function.() -> Match?): Function? =
	define(meta.value, defineNext)

fun Function.defineBeginToken(defineNext: Function.() -> Match?): Function? =
	define(beginByte, defineNext)

fun Function.defineEndToken(defineNext: Function.() -> Match?): Function? =
	define(endByte, defineNext)

fun Function.define(word: Word, defineNext: Function.() -> Match?): Function? =
	defineBit(
		word
			.letterStream
			.map(Letter::byte)
			.map(Byte::bitStream)
			.join,
		defineNext)

fun Function.define(byte: Byte, defineNext: Function.() -> Match?): Function? =
	defineBit(
		byte.bitStream,
		defineNext)

fun Function.defineBit(bitStream: Stream<Bit>, defineNext: Function.() -> Match?): Function? =
	bodyBinaryTrie.defineBit(bitStream) {
		defineNext.invoke(function)?.bodyBinaryTrieMatch
	}?.function

fun Function.define(bit: Bit, defineNext: Function.() -> Match?): Function? =
	bodyBinaryTrie.define(bit) {
		defineNext.invoke(function)?.bodyBinaryTrieMatch
	}?.function

// === reflect

val functionReflect: Field<Nothing>
	get() =
		functionWord fieldTo todoWord.term