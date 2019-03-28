package leo.base

fun appendableString(fn: (Appendable) -> Unit): String {
	val stringBuilder = StringBuilder()
	fn(stringBuilder)
	return stringBuilder.toString()
}

fun <V> V.string(fn: Appendable.(V) -> Unit): String {
	val stringBuilder = StringBuilder()
	stringBuilder.fn(this)
	return stringBuilder.toString()
}

fun Appendable.appendString(value: Any?): Appendable =
	append(value.string)

val String.byteStreamOrNull: Stream<Byte>?
	get() =
		toByteArray().streamOrNull

val String.bitStreamOrNull: Stream<EnumBit>?
	get() =
		byteStreamOrNull?.map(Byte::bitStream)?.join

val Stream<Byte>?.utf8string: String
	get() =
		appendableString { appendable ->
			appendable.fold(this) { byte ->
				append(byte.toChar())
			}
		}

val Stream<EnumBit>?.utf8String: String
	get() =
		this?.bitByteStreamOrNull?.utf8string ?: ""

fun <R> R.fold(charSequence: CharSequence, fn: R.(Char) -> R): R =
	charSequence.fold(this, fn)

fun String.mapChars(fn: (Char) -> Char) =
	StringBuilder(length).fold(this) { append(fn(it)) }.toString()

val String.utf8ByteArray
	get() =
		toByteArray()

fun String.codePointSeqAt(index: Int): Seq<Int> =
	Seq {
		if (index >= length) null
		else codePointAt(index).thenSeqNode(codePointSeqAt(offsetByCodePoints(index, 1)))
	}

val String.codePointSeq
	get() =
		codePointSeqAt(0)

val Seq<Int>.codePointString
	get() =
		StringBuilder().fold(this) {
			appendCodePoint(it)
		}.toString()