package leo.base

fun ByteArray.streamOrNullFrom(index: Int): Stream<Byte>? =
	if (index >= size) null
	else this[index].onlyStreamThen { streamOrNullFrom(index + 1) }

val ByteArray.streamOrNull: Stream<Byte>?
	get() =
		streamOrNullFrom(0)

val ByteArray.utf8String
	get() =
		String(this)

fun ByteArray.setInt(index: Int, value: Int) {
	var byteIndex = index.shl(2)
	this[byteIndex++] = value.ushr(24).toByte()
	this[byteIndex++] = value.ushr(16).toByte()
	this[byteIndex++] = value.ushr(8).toByte()
	this[byteIndex] = value.toByte()
}

fun ByteArray.getInt(index: Int): Int {
	var byteIndex = index.shl(2)
	var value = this[byteIndex++].toInt().and(0xFF).shl(24)
	value = value or this[byteIndex++].toInt().and(0xFF).shl(16)
	value = value or this[byteIndex++].toInt().and(0xFF).shl(8)
	value = value or this[byteIndex].toInt().and(0xFF)
	return value
}

val ByteArray.intRange
	get() =
		IntRange(0, size - 1)
