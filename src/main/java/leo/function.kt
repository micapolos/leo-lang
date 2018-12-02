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
	define(rule.patternTerm, null, rule.body)

fun Function.define(patternTerm: Term<Pattern>, backTraceOrNull: BackTrace?, body: Body): Function? =
	define(patternTerm, backTraceOrNull) { backTrace -> body.match }

fun Function.define(patternTerm: Term<Pattern>, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	defineToken(patternTerm.tokenStream, backTraceOrNull.push(patternTerm), defineNext)

fun Function.define(pattern: Pattern, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	when (pattern) {
		is OneOfPattern -> define(pattern, backTraceOrNull, defineNext)
		is RecursionPattern -> define(pattern, backTraceOrNull, defineNext)
	}

fun Function.define(pattern: OneOfPattern, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	orNull.fold(pattern.patternTermStream) { patternTerm ->
		this?.define(patternTerm, backTraceOrNull, defineNext)
	}

fun Function.define(pattern: RecursionPattern, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	define(pattern.recurse, backTraceOrNull, defineNext)

fun Function.define(recurse: Recurse, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	recurse.apply(backTraceOrNull)?.let { backTrace ->
		define(backTrace.patternTerm, backTrace.back, defineNext)
	}

fun Function.defineToken(tokenStream: Stream<Token<Pattern>>, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	orNull.read(tokenStream) { token, nextOrNull ->
		define(token, backTraceOrNull) {
			if (nextOrNull == null) defineNext(backTraceOrNull)
			else defineToken(nextOrNull, backTraceOrNull, defineNext)?.match
		}
	}

fun Function.define(token: Token<Pattern>, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	when (token) {
		is MetaToken -> define(token, backTraceOrNull, defineNext)
		is WordToken -> define(token, backTraceOrNull, defineNext)
		is ControlToken -> when (token.control) {
			is BeginControl -> defineBeginToken(backTraceOrNull, defineNext)
			is EndControl -> defineEndToken(backTraceOrNull, defineNext)
		}
	}

fun Function.define(token: MetaToken<Pattern>, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	define(token.meta, backTraceOrNull, defineNext)

fun Function.define(token: WordToken<Pattern>, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	define(token.word, backTraceOrNull, defineNext)

fun Function.define(meta: Meta<Pattern>, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	define(meta.value, backTraceOrNull, defineNext)

fun Function.defineBeginToken(backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	define(beginByte, backTraceOrNull, defineNext)

fun Function.defineEndToken(backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	define(endByte, backTraceOrNull, defineNext)

fun Function.define(word: Word, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	defineBit(
		word
			.letterStream
			.map(Letter::byte)
			.map(Byte::bitStream)
			.join,
		backTraceOrNull,
		defineNext)

fun Function.define(byte: Byte, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	defineBit(byte.bitStream, backTraceOrNull, defineNext)

fun Function.defineBit(bitStream: Stream<Bit>, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	bodyBinaryTrie.defineBit(bitStream) {
		function.defineNext(backTraceOrNull)?.bodyBinaryTrieMatch
	}?.function

fun Function.define(bit: Bit, backTraceOrNull: BackTrace?, defineNext: Function.(BackTrace?) -> Match?): Function? =
	bodyBinaryTrie.define(bit) {
		function.defineNext(backTraceOrNull)?.bodyBinaryTrieMatch
	}?.function

// === reflect

val functionReflect: Field<Nothing>
	get() =
		functionWord fieldTo todoWord.term