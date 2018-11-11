package leo

import leo.base.string

sealed class Token<out V> {
	data class Meta<V>(
		val value: V) : Token<V>() {
		override fun toString() = reflect.string
	}

	data class Identifier<V>(
		val word: Word) : Token<V>() {
		override fun toString() = reflect.string
	}

	data class Begin<V>(
		val unit: Unit) : Token<V>() {
		override fun toString() = reflect.string
	}

	data class End<V>(
		val unit: Unit) : Token<V>() {
		override fun toString() = reflect.string
	}
}

// === constructors

fun <V> token(word: Word) =
	Token.Identifier<V>(word)

fun <V> token(value: V) =
	Token.Meta(value)

fun <V> beginToken() =
	Token.Begin<V>(Unit)

fun <V> endToken() =
	Token.End<V>(Unit)

// === term ===

fun <V> Token<V>.term(): Term<V> =
	when (this) {
		is Token.Meta -> term(metaWord fieldTo term(value))
		is Token.Identifier -> term(identifierWord fieldTo term(word.reflect))
		is Token.Begin -> term(beginWord)
		is Token.End -> term(endWord)
	}

// == reflect

val <V> Token<V>.reflect: Field<Nothing>
	get() =
		reflect { valueWord fieldTo term(todoWord) }

fun <V> Token<V>.reflect(reflectValue: (V) -> Field<Nothing>): Field<Nothing> =
	tokenWord fieldTo when (this) {
		is Token.Meta -> term(metaWord fieldTo term(reflectValue(value)))
		is Token.Identifier -> term(identifierWord fieldTo term(word.reflect))
		is Token.Begin -> term(beginWord)
		is Token.End -> term(endWord)
	}