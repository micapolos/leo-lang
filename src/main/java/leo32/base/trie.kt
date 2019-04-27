package leo32.base

import leo.base.Seq
import leo.base.nullOf
import leo.binary.Bit

data class Trie<out T>(
	val tree: Tree<T?>)

val <T : Any> Tree<T?>.trie
	get() =
		Trie(this)

fun <T : Any> emptyTrie() =
	nullOf<T>().toLeaf.tree.trie

fun <T : Any> Trie<T>.uncheckedAt(bitSeq: Seq<Bit>): T? =
	tree.at(bitSeq)?.leafOrNull?.value

fun <T : Any> Trie<T>.uncheckedPut(bitSeq: Seq<Bit>, value: T?): Trie<T> =
	tree.updateWithDefault(bitSeq, { null }) { value.toLeaf.tree }.trie

fun <T: Any> Trie<T?>.atOrNull(bitSeq: Seq<Bit>): Trie<T>? =
	tree.at(bitSeq)?.trie