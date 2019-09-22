package leo13.base

import leo13.bitName
import leo13.fail
import leo13.oneName
import leo13.script.*
import leo13.script.Writer
import leo13.zeroName

val bitReader: Reader<Bit> =
	reader(bitName) {
		when (unsafeOnlyLine.unsafeOnlyName) {
			zeroName -> zeroBit
			oneName -> oneBit
			else -> fail(bitName)
		}
	}

val bitWriter: Writer<Bit> =
	writer(bitName) {
		if (isOne) script(oneName)
		else script(zeroName)
	}

data class Bit(val isOne: Boolean) {
	override fun toString() = bitWriter.string(this)
}

fun isOneBit(isOne: Boolean) = Bit(isOne)
val zeroBit = isOneBit(false)
val oneBit = isOneBit(true)
val Bit.negate: Bit get() = isOneBit(!isOne)

val Bit.int get() = if (isOne) 1 else 0