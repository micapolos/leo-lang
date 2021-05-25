package leo25.trie

fun <K, V> Trie<K, V>.get(key: K): V? {
	return get(key.hashCode(), key, 0)
}

fun <K, V> Trie<K, V>.put(key: K, value: V): Trie<K, V> =
	put(key.hashCode(), key, value, 0) ?: this

fun <K, V> Trie<K, V>.contains(key: K): Boolean =
	containsKey(key.hashCode(), key, 0)

fun <K, V> Trie<K, V>.remove(key: K): Trie<K, V> =
	remove(key.hashCode(), key, 0).let { newNode ->
		if (this === newNode) this
		else newNode ?: Trie.EMPTY as Trie<K, V>
	}
