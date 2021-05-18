package leo25

import kotlinx.collections.immutable.persistentMapOf
import leo.base.fold
import leo.base.nullOf

data class Word(val string: String)

sealed class Value
data class WordValue(val word: Word) : Value()
data class StringValue(val string: String) : Value()
data class LinkValue(val link: Link) : Value()
data class FunctionValue(val function: Function) : Value()

data class Link(val tail: Value?, val head: Line)
data class Line(val word: Word, val value: Value)

fun word(string: String) = Word(string)

operator fun Value?.plus(pair: Pair<String, Value?>): Value =
	pair.let { (string, valueOrNull) ->
		Word(string).let { word ->
			when {
				valueOrNull != null -> LinkValue(Link(this, Line(word, valueOrNull)))
				this != null -> LinkValue(Link(null, Line(word, this)))
				else -> WordValue(word)
			}
		}
	}

fun value(string: String): Value = StringValue(string)
fun value(pair: Pair<String, Value?>, vararg pairs: Pair<String, Value?>) =
	nullOf<Value>().plus(pair).fold(pairs) { plus(it) }

fun context() = Context(persistentMapOf())
