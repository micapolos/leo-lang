package leo13.base

import leo13.base.type.Type
import leo13.base.type.case
import leo13.base.type.toString
import leo13.base.type.type
import leo13.script.lineTo
import leo13.script.script

val bitType: Type<Bit> =
	type(
		"bit",
		case(type("zero")) { zeroBit },
		case(type("one")) { oneBit })
	{
		(if (isOne) "one" else "zero") lineTo script()
	}

data class Bit(val isOne: Boolean) {
	override fun toString() = bitType.toString(this)
}

fun isOneBit(isOne: Boolean) = Bit(isOne)
val zeroBit = isOneBit(false)
val oneBit = isOneBit(true)
val Bit.negate: Bit get() = isOneBit(!isOne)

val Bit.int get() = if (isOne) 1 else 0