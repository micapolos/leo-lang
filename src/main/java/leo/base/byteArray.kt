package leo.base

fun ByteArray.streamOrNullFrom(index: Int): Stream<Byte>? =
	if (index >= size) null
	else this[index].then { streamOrNullFrom(index + 1) }

val ByteArray.streamOrNull: Stream<Byte>?
	get() =
		streamOrNullFrom(0)