package leo32

import leo.base.*
import leo.binary.Bit
import leo.binary.zero
import leo32.base.*

typealias Seq32 = Seq<I32>

val I32.seq32
	get() =
		onlySeq

val Bit.seq32
	get() =
		i32.seq32

val Byte.seq32
	get() =
		i32.seq32

val Int.seq32
	get() =
		i32.seq32

val String.seq32
	get() =
		codePointSeq.map { i32 }

val Seq32.string32 get() =
	map { int }.codePointString

val Seq32.bitSeq get() =
	map { bitSeq }.flat

fun Seq<Bit>.bitSeq32(acc: I32, mask: I32): Seq32 =
	Seq {
		seqNodeOrNull?.let { seqNode ->
			acc.or(seqNode.first, mask).let { newAcc ->
				mask.shr1.let { newMask ->
					if (newMask.isZero) newAcc.then(seqNode.remaining.bitSeq32)
					else seqNode.remaining.bitSeq32(newAcc, newMask).seqNodeOrNull
				}
			}
		}
	}

//fun Seq<Bit>.bitByteSeq(acc: Byte, mask: Byte): Seq<Byte> =
//	Seq {
//		seqNodeOrNull?.let { seqNode ->
//			acc.or(seqNode.first, mask).let { newAcc ->
//				mask.ushr1.let { newMask ->
//					if (newMask == zero.byte) newAcc.then(seqNode.remaining.bitByteSeq)
//					else seqNode.remaining.bitByteSeq(newAcc, newMask).seqNodeOrNull
//				}
//			}
//		}
//	}
//
val Seq<Bit>.bitSeq32
	get() =
		bitSeq32(zero.i32, hsbI32)

//val Seq<Bit>.bitByteSeq
//	get() =
//		bitByteSeq(zero.byte, 0x80.toByte())
