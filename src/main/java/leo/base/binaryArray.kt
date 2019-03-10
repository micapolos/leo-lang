package leo.base

// Non-empty, power-of-two array of any value
// random access: O(log(n)) for values and sub-arrays
sealed class BinaryArray<out V>

data class SingleBinaryArray<V>(
	val value: V) : BinaryArray<V>() {
	override fun toString() = appendableString { it.append(this) }
}

data class CompositeBinaryArray<V>(
	val zeroBinaryArray: BinaryArray<V>,
	val oneBinaryArray: BinaryArray<V>) : BinaryArray<V>() {
	override fun toString() = appendableString { it.append(this) }
}

val BinaryArray<*>.depthInt: Int
	get() =
		when (this) {
			is SingleBinaryArray -> 0
			is CompositeBinaryArray -> zeroBinaryArray.depthInt + 1
		}

val <V> BinaryArray<V>.indexBitCount: BitCount
	get() =
		when (this) {
			is SingleBinaryArray -> bitBitCount
			is CompositeBinaryArray -> zeroBinaryArray.indexBitCount.increment
		}

val <V> BinaryArray<V>.incrementDepth: BinaryArray<V>
	get() =
		CompositeBinaryArray(this, this)

val <V> BinaryArray<V>.minIndexBinaryOrNull: Binary?
	get() =
		depthInt.sizeMinBinaryOrNull

val <V> BinaryArray<V>.maxIndexBinaryOrNull: Binary?
	get() =
		depthInt.sizeMaxBinaryOrNull

fun <V> BinaryArray<V>.increaseDepth(depth: Int): BinaryArray<V> =
	(depth - this.depthInt).let { delta ->
		if (delta > 0) iterate(delta) { incrementDepth }
		else this
	}

fun <V> Int.depthBinaryArray(value: V): BinaryArray<V> =
	if (this == 0)
		SingleBinaryArray(value)
	else CompositeBinaryArray(
		dec().depthBinaryArray(value),
		dec().depthBinaryArray(value))

fun <V> binaryArray(v0: V): BinaryArray<V> =
	SingleBinaryArray(v0)

fun <V> binaryArray(v0: V, v1: V): BinaryArray<V> =
	CompositeBinaryArray(
		binaryArray(v0),
		binaryArray(v1))

fun <V> binaryArray(v0: V, v1: V, v2: V, v3: V): BinaryArray<V> =
	CompositeBinaryArray(
		binaryArray(v0, v1),
		binaryArray(v2, v3))

fun <V> binaryArray(v0: V, v1: V, v2: V, v3: V, v4: V, v5: V, v6: V, v7: V): BinaryArray<V> =
	CompositeBinaryArray(
		binaryArray(v0, v1, v2, v3),
		binaryArray(v4, v5, v6, v7))

// === casting

val <V> BinaryArray<V>.singleOrNull: SingleBinaryArray<V>?
	get() =
		this as? SingleBinaryArray

val <V> BinaryArray<V>.compositeOrNull: CompositeBinaryArray<V>?
	get() =
		this as? CompositeBinaryArray

val <V> BinaryArray<V>.stream: Stream<V>
	get() =
		when (this) {
			is SingleBinaryArray -> value.onlyStream
			is CompositeBinaryArray -> zeroBinaryArray.stream.then { oneBinaryArray.stream }
		}

fun <V> Appendable.append(binaryArray: BinaryArray<V>): Appendable =
	this
		.append('[')
		.appendString(binaryArray.stream)
		.append(']')

fun <V : Any> BinaryArray<V>.get(index: Binary?): V? =
	subArrayOrNull(index)?.singleOrNull?.value

fun <V> BinaryArray<V>.subArrayOrNull(bit: EnumBit): BinaryArray<V>? =
	compositeOrNull?.run {
		when (bit) {
			EnumBit.ZERO -> zeroBinaryArray
			EnumBit.ONE -> oneBinaryArray
		}
	}

fun <V> BinaryArray<V>.subArrayOrNull(bitStream: Stream<EnumBit>?): BinaryArray<V>? =
	orNull.fold(bitStream) { bit ->
		this?.subArrayOrNull(bit)
	}

fun <V> BinaryArray<V>.subArrayOrNull(index: Binary?): BinaryArray<V>? =
	subArrayOrNull(index?.bitStream)

fun <V> BinaryArray<V>.set(binaryIndex: Binary?, value: V): BinaryArray<V>? =
	set(binaryIndex, binaryArray(value))

fun <V> BinaryArray<V>.set(binaryArray: BinaryArray<V>): BinaryArray<V>? =
	if (depthInt != binaryArray.depthInt) null
	else binaryArray

fun <V> BinaryArray<V>.set(bit: EnumBit, binaryArray: BinaryArray<V>): BinaryArray<V>? =
	compositeOrNull?.run {
		if (zeroBinaryArray.depthInt != binaryArray.depthInt) null
		else when (bit) {
			EnumBit.ZERO -> CompositeBinaryArray(binaryArray, oneBinaryArray)
			EnumBit.ONE -> CompositeBinaryArray(zeroBinaryArray, binaryArray)
		}
	}

fun <V> BinaryArray<V>.set(bitStreamOrNull: Stream<EnumBit>?, binaryArray: BinaryArray<V>): BinaryArray<V>? =
	if (bitStreamOrNull == null) set(binaryArray)
	else bitStreamOrNull.nextOrNull.let { nextBitStreamOrNull ->
		if (nextBitStreamOrNull == null)
			set(bitStreamOrNull.first, binaryArray)
		else subArrayOrNull(bitStreamOrNull.first)
			?.set(nextBitStreamOrNull, binaryArray)
			?.let { updatedNextArray -> set(bitStreamOrNull.first, updatedNextArray) }
	}

fun <V> BinaryArray<V>.set(binary: Binary?, binaryArray: BinaryArray<V>): BinaryArray<V>? =
	set(binary?.bitStream, binaryArray)

// === indexed stream

val <V> BinaryArray<V>.indexedBitStream: Stream<Indexed<V>>
	get() =
		stream.wrapIndexedStream(minIndexBinaryOrNull)

// === mapping

fun <V, R> BinaryArray<V>.map(fn: V.() -> R): BinaryArray<R> =
	when (this) {
		is SingleBinaryArray -> binaryArray(fn(value))
		is CompositeBinaryArray -> CompositeBinaryArray(zeroBinaryArray.map(fn), oneBinaryArray.map(fn))
	}
