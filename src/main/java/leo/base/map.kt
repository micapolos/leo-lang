package leo.base

data class Map<in K, out V>(
	val binaryTrie: BinaryTrie<V>,
	val keyBitStreamOrNullFn: (K) -> Stream<Bit>?)

fun <K, V> emptyMap(keyBitStreamOrNullFn: (K) -> Stream<Bit>?): Map<K, V> =
	Map(emptyBinaryTrie(), keyBitStreamOrNullFn)

fun <K, V> Map<K, V>.get(key: K): The<V>? =
	binaryTrie.match(binaryTrieKeyBitStream(key))?.theValueOrNull

fun <K, V> Map<K, V>.set(key: K, value: V): Map<K, V> =
	copy(binaryTrie = binaryTrie.set(binaryTrieKeyBitStream(key), value))

fun <K, V> Map<K, V>.binaryTrieKeyBitStream(key: K): Stream<Bit> =
	keyBitStreamOrNullFn(key)
		?.map { bit -> stream(Bit.ONE, bit) }
		?.join
		?.then { Bit.ZERO.onlyStream }
		?: Bit.ZERO.onlyStream