package leo

import leo.base.*

data class TokenEvaluator(
	val entryStackOrNull: Stack<Entry>?,
	val scope: Scope) {
	//override fun toString() = reflect.string

	data class Entry(
		val scope: Scope,
		val word: Word) {
		//override fun toString() = reflect.string
	}
}

val emptyTokenEvaluator =
	TokenEvaluator(null, emptyScope)

fun TokenEvaluator.evaluate(token: Token<Nothing>): TokenEvaluator? =
	when (token) {
		is Token.MetaEnd -> fail
		is Token.Begin -> begin(token.word)
		is Token.End -> end
	}

fun TokenEvaluator.begin(word: Word): TokenEvaluator =
	copy(
		entryStackOrNull = entryStackOrNull.push(TokenEvaluator.Entry(scope, word)),
		scope = Scope(scope.function, null))

val TokenEvaluator.end: TokenEvaluator?
	get() =
		entryStackOrNull?.let { entryStack ->
			copy(
				entryStackOrNull = entryStack.pop,
				scope = entryStack.top.scope.push(entryStack.top.word fieldTo scope.termOrNull).evaluate)
		}

// === reflect ===

//val TokenEvaluator.reflect: Field<Nothing>
//	get() =
//		tokenWord fieldTo term(
//			readerWord fieldTo term(
//				entryStackOrNull.orNullReflect(entryWord) { reflect(entryWord, TokenEvaluator.Entry::reflect) },
//				scope.reflect))
//
//val TokenEvaluator.Entry.reflect: Field<Nothing>
//	get() =
//		entryWord fieldTo term(
//			scope.reflect,
//			word.reflect)
//
// === byte stream

val TokenEvaluator.bitStreamOrNull: Stream<Bit>?
	get() =
		nullOf<Stream<Bit>>()
			.fold(entryStackOrNull?.reverse?.stream) { entry ->
				orNullThen(entry.coreBitStream).then('('.clampedByte.bitStream)
			}
			.orNullThenIfNotNull(scope.bitStreamOrNull)

val TokenEvaluator.Entry.coreBitStream: Stream<Bit>
	get() =
		scope.bitStreamOrNull.orNullThen(word.bitStream)

fun TokenEvaluator.apply(term: Term<Nothing>): Term<Nothing>? =
	scope.apply(term)