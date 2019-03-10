package leo.binary

import leo.base.Seq
import leo.base.emptySeq
import leo.base.intRange
import leo.base.then

fun ByteArray.bitSeq(range: IntRange): Seq<Bit> =
	if (range.isEmpty()) emptySeq()
	else this[range.first].bitSeq.then {
		bitSeq(IntRange(range.first + 1, range.last))
	}

val ByteArray.bitSeq: Seq<Bit>
	get() =
		bitSeq(intRange)
