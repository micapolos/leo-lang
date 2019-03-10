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

fun <V> BinaryMap<V>.get(bit: EnumBit): V =
	when (bit) {
		EnumBit.ZERO -> zeroValue
		EnumBit.ONE -> oneValue
	}

fun <V> BinaryMap<V>.set(bit: EnumBit, value: V): BinaryMap<V> =
	when (bit) {
		EnumBit.ZERO -> copy(zeroValue = value)
		EnumBit.ONE -> copy(oneValue = value)
	}
