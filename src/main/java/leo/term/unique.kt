package leo.term

import leo.uniqueWord

data class Unique<out V>(
	val value: V) {
	override fun toString() = "unique $value"
}

val <V> V.unique: Unique<V>
	get() =
		Unique(this)

fun <V> unique(value: V): Unique<V> =
	Unique(value)

fun <V : Any> Script.parseUnique(parseValue: Script?.() -> V?): Unique<V>? =
	matchArgument(uniqueWord) {
		parseValue()?.unique
	}