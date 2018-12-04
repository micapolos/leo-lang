package leo.base

// Binary key, which is not a prefix of any other key.
data class BinaryKey(
	val bitStream: Stream<Bit>)

val Stream<Bit>.binaryKey: BinaryKey
	get() =
		BinaryKey(this)
