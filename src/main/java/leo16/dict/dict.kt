package leo16.dict

import kotlinx.collections.immutable.PersistentMap
import leo.base.notNullIf
import leo.base.orNull
import leo.base.orNullFold
import leo13.seq
import leo16.ChoiceField
import leo16.DictionaryField
import leo16.Field
import leo16.FunctionField
import leo16.LazyField
import leo16.NativeField
import leo16.Pattern
import leo16.Sentence
import leo16.SentenceField
import leo16.Value

data class Dict<out T>(
	val entry: Entry<T>,
	val endNodeOrNull: Node<T>?)

sealed class Node<out T>
data class DictNode<T>(val dict: Dict<T>) : Node<T>()
data class LeafNode<T>(val value: T) : Node<T>()

sealed class Entry<out T>
data class AnyEntry<T>(val unit: Unit = Unit) : Entry<T>()
data class MapEntry<T>(val map: PersistentMap<String, Node<T>>) : Entry<T>()
data class FunctionEntry<T>(val pattern: Pattern) : Entry<T>()

val <T> Node<T>.dictOrNull: Dict<T>? get() = (this as? DictNode)?.dict
val <T : Any> Node<T>.leafOrNull: T? get() = (this as? LeafNode)?.value

val <T> Entry<T>.isAny get() = this is AnyEntry
fun <T> Entry<T>.nodeOrNull(word: String) = (this as? MapEntry)?.map?.get(word)
val <T> Entry<T>.functionPatternOrNull: Pattern? get() = (this as? FunctionEntry)?.pattern

fun <T> Node<T>.get(value: Value): Node<T>? =
	orNull.orNullFold(value.fieldStack.seq) { get(it) }

fun <T> Node<T>.get(field: Field): Node<T>? =
	when (field) {
		is SentenceField -> get(field.sentence)
		is FunctionField -> TODO()
		is DictionaryField -> TODO()
		is NativeField -> TODO()
		is ChoiceField -> TODO()
		is LazyField -> TODO()
	}

fun <T> Node<T>.get(sentence: Sentence): Node<T>? =
	when (this) {
		is DictNode -> dict.getNode(sentence)
		is LeafNode -> null
	}

fun <T> Dict<T>.getNode(sentence: Sentence): Node<T>? =
	when (entry) {
		is AnyEntry -> TODO()
		is MapEntry -> TODO()
		is FunctionEntry -> TODO()
	}
