package leo32.base

sealed class Chain<out K, out V>

data class LeafChain<K, V>(
	val value: V
) : Chain<K, V>()

data class LinkChain<K, V>(
	val chain: Chain<K, V>,
	val value: K
) : Chain<K, V>()

fun <K, V> V.chain() =
	LeafChain<K, V>(this) as Chain<K, V>

fun <K, V> Chain<K, V>.plus(value: K) =
	LinkChain(this, value) as Chain<K, V>
