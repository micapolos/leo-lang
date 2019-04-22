package leo32

import leo.base.*
import leo.binary.utf8ByteSeq

val Seq<Byte>.utf8IntSeq: Seq<Int>
	get() =
		Seq { seqNodeOrNull?.utf8IntSeq?.seqNodeOrNull }

val SeqNode<Byte>.utf8IntSeq: Seq<Int>
	get() =
		first.uint.let { int ->
			when {
				int.and(0b10000000) == 0b00000000 ->
					remaining.utf8IntSeq(int.and(0b01111111), 0)
				int.and(0b11100000) == 0b11000000 ->
					remaining.utf8IntSeq(int.and(0b00011111), 1)
				int.and(0b11110000) == 0b11100000 ->
					remaining.utf8IntSeq(int.and(0b00001111), 2)
				int.and(0b11111000) == 0b11110000 ->
					remaining.utf8IntSeq(int.and(0b00000111), 3)
				int.and(0b11111100) == 0b11111000 ->
					remaining.utf8IntSeq(int.and(0b00000011), 4)
				int.and(0b11111110) == 0b11111100 ->
					remaining.utf8IntSeq(int.and(0b00000001), 5)
				else -> remaining.utf8IntSeq
			}
		}

fun Seq<Byte>.utf8IntSeq(acc: Int, size: Int): Seq<Int> =
	Seq { seqNodeOrNull?.utf8IntSeq(acc, size)?.seqNodeOrNull }

fun SeqNode<Byte>.utf8IntSeq(acc: Int, size: Int): Seq<Int> =
	first.uint.let { int ->
		when {
			int.and(0x11000000) == 0x10000000 ->
				remaining.utf8IntSeq(acc.shl(6).or(int.and(0x00111111)), size.dec())
			else -> remaining.utf8IntSeq
		}
	}

// TODO(micapolos): Implement natively
val Int.utf8Seq: Seq<Byte>
	get() =
		if (this < 0) emptySeq()
		else StringBuilder()
			.appendCodePoint(this)
			.toString()
			.utf8ByteSeq

val Seq<Int>.utf8Seq: Seq<Byte>
	get() =
		map { utf8Seq }.flat