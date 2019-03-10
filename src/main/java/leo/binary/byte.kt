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
