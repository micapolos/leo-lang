@file:Suppress("unused")

package leo32.base

import leo.base.Empty

data class Dict<K, V : Any>(
	val trie: Trie<V>,
	val dictKey: K.() -> DictKey)

fun <K, V: Any> Empty.dict(dictKey: K.() -> DictKey) =
	emptyTrie<V>().dict(dictKey)

fun <K, V : Any> Trie<V>.dict(dictKey: K.() -> DictKey) =
	Dict(this, dictKey)

fun <K, V : Any> Dict<K, V>.at(key: K) =
	trie.uncheckedAt(key.dictKey().bitSeq)

fun <K, V : Any> Dict<K, V>.dictOrNullAt(key: K): Dict<K, V>? =
	trie.atOrNull(key.dictKey().bitSeq)?.let { copy(trie = it) }

fun <K, V : Any> Dict<K, V>.put(key: K, value: V) =
	copy(trie = trie.uncheckedPut(key.dictKey().bitSeq, value))

fun <K, V: Any> Dict<K, V>.update(key: K, fn: V?.() -> V) =
	put(key, at(key).fn())

fun <K, V : Any> Dict<K, V>.computeAt(key: K, compute: () -> V): Effect<Dict<K, V>, Pair<V, Boolean>> =
	key.dictKey().bitSeq.let { keyBitSeq ->
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
	emptyTrie<V>().dict { dictKey }
