package leo32.base

import leo.base.byte
import leo.base.updateByte

data class IntToByteRam(val intArray: Ram<Int>) : Ram<Byte> {
	override fun at(index: Int) =
		intArray.at(index.ushr(2)).byte(3 - index.and(3))

	override fun update(index: Int, fn: Byte.() -> Byte) =
		intArray.update(index.ushr(2)) {
			updateByte(3 - index.and(3), fn)
		}.intToByte
}

val Ram<Int>.intToByte
	get() =
		IntToByteRam(this)
