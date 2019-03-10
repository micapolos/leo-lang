package leo.binary

import leo.base.Seq
import leo.base.SeqNode
import leo.base.emptySeq
import leo.base.int

inline val Byte.bit7: Bit
	get() =
		int.and(0x80).clampedBit

inline val Byte.bit6: Bit
	get() =
		int.and(0x40).clampedBit

inline val Byte.bit5: Bit
	get() =
		int.and(0x20).clampedBit

inline val Byte.bit4: Bit
	get() =
		int.and(0x10).clampedBit

inline val Byte.bit3: Bit
	get() =
		int.and(0x08).clampedBit

inline val Byte.bit2: Bit
	get() =
		int.and(0x04).clampedBit

inline val Byte.bit1: Bit
	get() =
		int.and(0x02).clampedBit

inline val Byte.bit0: Bit
	get() =
		int.and(0x01).clampedBit

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
