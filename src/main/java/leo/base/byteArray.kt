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