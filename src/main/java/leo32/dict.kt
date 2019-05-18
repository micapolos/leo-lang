package leo32

import leo.base.*
import leo.binary.Bit
import leo.binary.byteBitSeq
import leo32.base.*
import leo32.base.Tree

data class Dict<K, V : Any>(
	val tree: Tree<V?>,
	val getBitSeq: K.() -> Seq<Bit>)

fun <K, V : Any> Empty.dict(getBitSeq: K.() -> Seq<Bit>) =
	Dict<K, V>(tree(), getBitSeq)

fun <T : Any> Empty.symbolDict() =
	dict<Symbol, T> { byteSeq.byteBitSeq }

fun <K, V : Any> Dict<K, V>.contains(key: K): Boolean =
	at(key) != null

fun <K, V : Any> Dict<K, V>.at(bit: Bit): Dict<K, V>? =
	tree.at(bit)?.let { treeAtBit ->
		copy(tree = treeAtBit)
	}

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

val <V : Any> Dict<*, V>.valueSeq: Seq<V>
	get() =
		tree.seq.filterMap { this?.the }

fun <V : Any> Dict<*, V>.eq(dict: Dict<*, V>, fn: V.(V) -> Boolean): Boolean =
	tree.eq(dict.tree) { nullableEq(it, fn) }

fun <V : Any> Dict<*, V>.contains(dict: Dict<*, V>, fn: V.(V) -> Boolean): Boolean =
	tree.contains(dict.tree) { nullableContains(it, fn) }

fun <K, V : Any> Dict<K, V>.computeIfAbsent(key: K, fn: () -> V): Dict<K, V> =
	if (!contains(key)) put(key, fn())
	else this

val Dict<*, *>.isEmpty
	get() = tree is LeafTree
