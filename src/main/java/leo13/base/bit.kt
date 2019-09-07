package leo13.base

import leo13.script.lineTo
import leo13.script.script
import leo13.scripter.Scripter
import leo13.scripter.case
import leo13.scripter.scripter
import leo13.scripter.toString

val bitScripter: Scripter<Bit> =
	scripter(
		"bit",
		case(scripter("zero")) { zeroBit },
		case(scripter("one")) { oneBit })
	{
		(if (isOne) "one" else "zero") lineTo script()
	}

data class Bit(val isOne: Boolean) {
	override fun toString() = bitScripter.toString(this)
}

fun isOneBit(isOne: Boolean) = Bit(isOne)
val zeroBit = isOneBit(false)
val oneBit = isOneBit(true)
val Bit.negate: Bit get() = isOneBit(!isOne)

val Bit.int get() = if (isOne) 1 else 0