package leo32

import leo.base.Writer
import leo.base.write
import leo.binary.*

val Writer<Bit>.byteToBit: Writer<Byte>
	get() =
		Writer { byte ->
			this
				.write(byte.bit7)
				.write(byte.bit6)
				.write(byte.bit5)
				.write(byte.bit4)
				.write(byte.bit3)
				.write(byte.bit2)
				.write(byte.bit1)
				.write(byte.bit0)
				.byteToBit
		}