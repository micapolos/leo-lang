package leo.binary

import leo.base.*

val Byte.bitSeq
	get() =
		Seq {
			SeqNode(bit7, Seq {
				SeqNode(bit6, Seq {
					SeqNode(bit5, Seq {
						SeqNode(bit4, Seq {
							SeqNode(bit3, Seq {
								SeqNode(bit2, Seq {
									SeqNode(bit1, Seq {
										SeqNode(bit0, emptySeq())
									})
								})
							})
						})
					})
				})
			})
		}

val Seq<Byte>.byteBitSeq
	get() =
		map { bitSeq }.flat

val Seq<Bit>.bitByteSeq
	get() =
		bitByteSeq(0, 0x80)

fun Seq<Bit>.bitByteSeq(acc: Int, mask: Int): Seq<Byte> =
	Seq {
		seqNodeOrNull?.let { bitSeqNode ->
			acc.or(if (bitSeqNode.first.isZero) 0 else mask).let { newAcc ->
				mask.ushr(1).let { newMask ->
					if (newMask == 0) newAcc.clampedByte.then(bitSeqNode.remaining.bitByteSeq)
					else bitSeqNode.remaining.bitByteSeq(newAcc, newMask).seqNodeOrNull
				}
			}
		}
	}
