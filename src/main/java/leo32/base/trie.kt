package leo32.base

import leo.base.Seq
import leo.base.nullOf
import leo.binary.Bit

data class Trie<out T>(
	val tree: Tree<T?>)

val <T : Any> Tree<T?>.dict
	get() =
		Trie(this)

fun <T : Any> emptyTrie() =
	nullOf<T>().leaf.tree.dict

fun <T : Any> Trie<T>.uncheckedAt(bitSeq: Seq<Bit>): T? =
	tree.at(bitSeq)?.leafOrNull?.value

fun <T : Any> Trie<T>.uncheckedPut(bitSeq: Seq<Bit>, value: T?): Trie<T> =
	tree.update(bitSeq) { value.leaf.tree }.dict
