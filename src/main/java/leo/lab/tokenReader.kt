package leo.lab

import leo.*
import leo.base.*

data class TokenReader(
	val entryStackOrNull: Stack<Entry>?,
	val scope: Scope) {
	override fun toString() = reflect.string

	data class Entry(
		val scope: Scope,
		val word: Word) {
		override fun toString() = reflect.string
	}
}

val emptyTokenReader =
	TokenReader(null, emptyScope)

fun TokenReader.begin(word: Word): TokenReader =
	copy(
		entryStackOrNull = entryStackOrNull.push(TokenReader.Entry(scope, word)),
		scope = Scope(scope.function, null))

val TokenReader.end: TokenReader?
	get() =
		entryStackOrNull?.let { entryStack ->
			copy(
				entryStackOrNull = entryStack.pop,
				scope = entryStack.top.scope.push(entryStack.top.word fieldTo scope.termOrNull).evaluate)
		}

// === reflect ===

val TokenReader.reflect: Field<Nothing>
	get() =
		tokenWord fieldTo term(
			readerWord fieldTo term(
				entryStackOrNull.orNullReflect(entryWord) { reflect(entryWord, TokenReader.Entry::reflect) },
				scope.reflect))

val TokenReader.Entry.reflect: Field<Nothing>
	get() =
		entryWord fieldTo term(
			scope.reflect,
			word.reflect)

// === byte stream

val TokenReader.byteStreamOrNull: Stream<Byte>?
	get() =
		nullOf<Stream<Byte>>()
			.fold(entryStackOrNull?.reverse?.stream) { entry ->
				orNullThen(entry.byteStream).then('('.toByte().onlyStream)
			}
			.orNullThenIfNotNull(scope.byteStreamOrNull)

val TokenReader.Entry.byteStream: Stream<Byte>
	get() =
		scope.byteStreamOrNull.orNullThen(word.byteStream)
