package leo.base

data class Map<in K, out V>(
	val binaryTrie: BinaryTrie<V>,
	val keyBitStreamOrNullFn: (K) -> Stream<EnumBit>?)

fun <K, V> emptyMap(keyBitStreamOrNullFn: (K) -> Stream<EnumBit>?): Map<K, V> =
	Map(emptyBinaryTrie(), keyBitStreamOrNullFn)

fun <K, V : Any> Map<K, V>.get(key: K): V? =
	binaryTrie.matchOrNull(binaryTrieKeyBitStream(key))?.theValueOrNull?.value

fun <K, V> Map<K, V>.set(key: K, value: V): Map<K, V> =
	copy(binaryTrie = binaryTrie.set(binaryTrieKeyBitStream(key), value))

fun <K, V> Map<K, V>.set(pair: Pair<K, V>): Map<K, V> =
	set(pair.first, pair.second)

fun <K, V> Map<K, V>.binaryTrieKeyBitStream(key: K): Stream<EnumBit> =
	keyBitStreamOrNullFn(key)
		?.map { bit -> stream(EnumBit.ONE, bit) }
		?.join
		?.then { EnumBit.ZERO.onlyStream }
		?: EnumBit.ZERO.onlyStream

fun <K, V> map(keyBitStreamOrNullFn: K.() -> Stream<EnumBit>?, vararg pairs: Pair<K, V>): Map<K, V> =
	emptyMap<K, V>(keyBitStreamOrNullFn).fold(pairs, Map<K, V>::set)