package leo.binary

import leo.base.*
import java.io.ByteArrayOutputStream

fun ByteArray.bitSeq(range: IntRange): Seq<Bit> =
	if (range.isEmpty()) emptySeq()
	else this[range.first].bitSeq.then {
		bitSeq(IntRange(range.first + 1, range.last))
	}

fun ByteArray.seq(start: Int): Seq<Byte> =
	Seq {
		notNullIf(start < size) {
			this[start].then(seq(start.inc()))
		}
	}

val ByteArray.seq
	get() =
		seq(0)

val ByteArray.bitSeq: Seq<Bit>
	get() =
		bitSeq(intRange)

val Seq<Byte>.byteArray
	get() =
		ByteArrayOutputStream()
			.fold(this) { byte ->
				apply {
					write(byte.uint)
				}
			}
			.toByteArray()
