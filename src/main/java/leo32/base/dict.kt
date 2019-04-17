@file:Suppress("unused")

package leo32.base

import leo.base.Empty
import leo.base.Seq
import leo.binary.Bit
import leo32.bitSeq
import leo32.seq32

data class Dict<K, V : Any>(
	val trie: Trie<V>,
	val bitSeq: K.() -> Seq<Bit>)

fun <K, V : Any> Empty.dict(bitSeq: K.() -> Seq<Bit>) =
	emptyTrie<V>().dict(bitSeq)

fun <K, V : Any> Trie<V>.dict(bitSeq: K.() -> Seq<Bit>) =
	Dict(this, bitSeq)

fun <K, V : Any> Dict<K, V>.at(key: K) =
	trie.uncheckedAt(key.bitSeq())

fun <K, V : Any> Dict<K, V>.dictOrNullAt(key: K): Dict<K, V>? =
	trie.atOrNull(key.bitSeq())?.let { copy(trie = it) }

fun <K, V : Any> Dict<K, V>.put(key: K, value: V) =
	copy(trie = trie.uncheckedPut(key.bitSeq(), value))

fun <K, V: Any> Dict<K, V>.update(key: K, fn: V?.() -> V) =
	put(key, at(key).fn())

fun <K, V : Any> Dict<K, V>.computeAt(key: K, compute: () -> V): Effect<Dict<K, V>, Pair<V, Boolean>> =
	key.bitSeq().let { keyBitSeq ->
		trie.uncheckedAt(keyBitSeq).let { value ->
			if (value == null) compute().let { newValue ->
				copy(trie = trie.uncheckedPut(keyBitSeq, newValue)).effect(newValue to true)
			}
			else effect(value to false)
		}
	}

val <K, V: Any> Dict<K, V>.valueOrNull: V? get() =
	trie.tree.leafOrNull?.value

// === core dicts ===

@Suppress("unused")
fun <V : Any> Empty.stringDict(): Dict<String, V> =
	emptyTrie<V>().dict { seq32.bitSeq }
