package leo13.base

import leo13.fail
import leo13.script.*
import leo13.script.Writer

val bitName = "bit"

val bitReader: Reader<Bit> =
	reader(bitName) {
		when (unsafeOnlyLine.unsafeOnlyName) {
			"zero" -> zeroBit
			"one" -> oneBit
			else -> fail("bit")
		}
	}

val bitWriter: Writer<Bit> =
	writer(bitName) {
		if (isOne) script("one")
		else script("zero")
	}

data class Bit(val isOne: Boolean) {
	override fun toString() = bitWriter.string(this)
}

fun isOneBit(isOne: Boolean) = Bit(isOne)
val zeroBit = isOneBit(false)
val oneBit = isOneBit(true)
val Bit.negate: Bit get() = isOneBit(!isOne)

val Bit.int get() = if (isOne) 1 else 0