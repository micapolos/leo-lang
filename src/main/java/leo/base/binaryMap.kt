package leo.base

data class BinaryMap<out V>(
	val zeroValue: V,
	val oneValue: V)

val <V> V.binaryMap: BinaryMap<V>
	get() =
		BinaryMap(this, this)

fun <V> binaryMap(): BinaryMap<V?> =
	BinaryMap(null, null)

fun <V> binaryMap(zeroValue: V, oneValue: V) =
	BinaryMap(zeroValue, oneValue)

fun <V> BinaryMap<V>.get(bit: Bit): V =
	when (bit) {
		Bit.ZERO -> zeroValue
		Bit.ONE -> oneValue
	}

fun <V> BinaryMap<V>.set(bit: Bit, value: V): BinaryMap<V> =
	when (bit) {
		Bit.ZERO -> copy(zeroValue = value)
		Bit.ONE -> copy(oneValue = value)
	}
