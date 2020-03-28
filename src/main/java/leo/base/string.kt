package leo.base

fun appendableString(fn: (Appendable) -> Unit): String {
	val stringBuilder = StringBuilder()
	fn(stringBuilder)
	return stringBuilder.toString()
}

fun appendableIndentedString(fn: (AppendableIndented) -> Unit): String =
	appendableString { fn(it.indented) }

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
		else codePointAt(index) then codePointSeqAt(offsetByCodePoints(index, 1))
	}

val String.codePointSeq
	get() =
		codePointSeqAt(0)

val Seq<Int>.codePointString
	get() =
		StringBuilder().fold(this) {
			appendCodePoint(it)
		}.toString()

val Int.codePointString: String get() =
	StringBuilder().appendCodePoint(this).toString()

fun String.charSeqAt(index: Int): Seq<Char> =
	Seq {
		notNullIf(index < length) {
			this[index] then charSeqAt(index.inc())
		}
	}

val String.charSeq
	get() =
		charSeqAt(0)

val Seq<Char>.charString
	get() =
		appendableString { fold(it, Appendable::append) }

val String.parenthesized get() = "($this)"

fun String.indentNewlines(indent: Int) =
	replace("\n", "\n" + indent.indentString)

val Int.indentString: String
	get() =
		"  ".repeat(this)

val bomString = "\uFEFF"