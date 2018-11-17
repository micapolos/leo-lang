package leo.base

fun appendableString(fn: (Appendable) -> Unit): String {
	val stringBuilder = StringBuilder()
	fn(stringBuilder)
	return stringBuilder.toString()
}

fun Appendable.appendString(value: Any?): Appendable =
	append(value.string)

val String.byteStreamOrNull: Stream<Byte>?
	get() =
		toByteArray().streamOrNull

val String.bitStreamOrNull: Stream<Bit>?
	get() =
		byteStreamOrNull?.mapNotNull(Byte::bitStream)?.join

val Stream<Byte>?.utf8string
	get() =
		appendableString { appendable ->
			appendable.fold(this) { byte ->
				append(byte.toChar())
			}
		}