package leo13.generic

import leo.base.fold
import leo.base.notNullIf
import leo.base.nullOf
import leo.base.updateIfNotNull
import leo13.*

data class List<out V>(val tail: List<V>?, val head: V)

fun <V> list(tail: List<V>?, head: V) =
	List(tail, head)

infix fun <V> List<V>?.orNullPlus(head: V): List<V> =
	list(this, head)

infix fun <V> List<V>.plus(head: V): List<V> =
	list(this, head)

fun <V> list(value: V, vararg values: V): List<V> =
	list(null, value).fold(values) { plus(it) }

fun <V> listOrNull(vararg values: V): List<V>? =
	nullOf<List<V>>().fold(values) { orNullPlus(it) }

fun <V, R> SentenceLineWriter<V>.map(fn: R.() -> V): SentenceLineWriter<R> =
	sentenceWriter(word) { bodySentence(fn()) }

fun <V> listSentenceWriter(word: Word, itemWriter: SentenceLineWriter<V>): SentenceLineWriter<List<V>> =
	sentenceWriter(word) {
		reverse.run {
			sentence(itemWriter.sentenceLine(head)).foldOrNull(tail) {
				plus(itemWriter.sentenceLine(it))
			}
		}
	}

fun <V, R: Any> R.foldUntilNull(list: List<V>, fn: R.(V) -> R?): R =
	fn(list.head).let { foldedOrNull ->
		foldedOrNull?.updateIfNotNull(list.tail) { foldUntilNull(it, fn) } ?: this
	}

fun <V, R: Any> R.foldOrNullUntilNull(listOrNull: List<V>?, fn: R.(V) -> R?): R =
	updateIfNotNull(listOrNull) { foldUntilNull(it, fn) }

fun <V, R> R.fold(list: List<V>, fn: R.(V) -> R): R =
	fn(list.head).updateIfNotNull(list.tail) { fold(it, fn) }

fun <V, R> R.foldOrNull(listOrNull: List<V>?, fn: R.(V) -> R): R =
	updateIfNotNull(listOrNull) { fold(it, fn) }

val <V> List<V>.reverse: List<V>
	get() =
		list(head).foldOrNull(tail) { plus(it) }

val <V> List<V>?.orNullReverse: List<V>? get() =
	nullOf<List<V>>().foldOrNull(this) { orNullPlus(it) }

fun <V, R> List<V>.map(fn: V.() -> R): List<R> =
	list(head.fn()).foldOrNull(tail) { plus(it.fn()) }.reverse

fun <V, R> List<V>?.orNullMap(fn: V.() -> R): List<R>? =
	nullOf<List<R>>().foldOrNull(this) { orNullPlus(it.fn()) }.orNullReverse

fun <V, R : Any> List<V>.mapFirstOrNull(fn: V.() -> R?): R? =
	head.fn() ?: tail?.mapFirstOrNull(fn)

fun <V> List<V>.any(fn: V.() -> Boolean): Boolean =
	mapFirstOrNull { notNullIf(fn()) { Unit } } != null

val <V : Any> List<V>.onlyHeadOrNull: V?
	get() =
		notNullIf(tail == null) { head }

val List<*>.hasOnlyHead: Boolean
	get() =
		tail == null