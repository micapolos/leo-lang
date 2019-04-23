package leo32.runtime

import leo.base.Empty
import leo.base.Seq
import leo.base.fail
import leo.binary.Bit
import leo.binary.byteBitSeq
import leo32.base.*

data class Dict<K, V : Any>(
	val tree: Tree<V?>,
	val getBitSeq: K.() -> Seq<Bit>)

fun <K, V : Any> Empty.dict(getBitSeq: K.() -> Seq<Bit>) =
	Dict<K, V>(tree(), getBitSeq)

fun <T : Any> Empty.symbolDict() =
	dict<Symbol, T> { byteSeq.byteBitSeq }

fun <K, V : Any> Dict<K, V>.at(key: K): V? =
	tree.at(key.getBitSeq())?.let { tree ->
		when (tree) {
			is LeafTree -> tree.leaf.value
			is BranchTree -> fail()
		}
	}

fun <K, V : Any> Dict<K, V>.update(key: K, fn: V?.() -> V): Dict<K, V> =
	copy(tree = tree.update(key.getBitSeq()) {
		if (this == null) fn()
		else when (this) {
			is LeafTree -> leaf.value.fn()
			is BranchTree -> fail()
		}
	})

fun <K, V : Any> Dict<K, V>.put(key: K, value: V) =
	update(key) { value }