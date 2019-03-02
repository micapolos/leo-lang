package leo.binary

import leo.base.*

data class Binary(
	val headBit: Bit,
	val tailBinaryOrNull: Binary?) {
	override fun toString() = appendableString {
		it.append("0b").appendDigits(this)
	}
}

val Bit.binary
	get() =
		Binary(this, null)

fun binary(bit: Bit, vararg bits: Bit) =
	Binary(bit, bits.foldRight(null, ::Binary))

val Binary.bitStream: Stream<Bit>
	get() =
		headBit.onlyStream.then { tailBinaryOrNull?.bitStream }

fun Appendable.appendDigits(binary: Binary): Appendable =
	fold(binary.bitStream) { append(it.char) }
