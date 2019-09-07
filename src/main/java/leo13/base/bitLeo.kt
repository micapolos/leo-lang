package leo13.base

import leo.binary.*
import leo13.base.type.Type
import leo13.base.type.case
import leo13.base.type.toString
import leo13.base.type.type
import leo13.script.lineTo
import leo13.script.script

val bitType: Type<Bit> =
	type(
		"bit",
		case(type("zero")) { zero.bit },
		case(type("one")) { one.bit })
	{
		(if (isZero) "zero" else "one") lineTo script()
	}

data class BitLeo(val bit: Bit) {
	override fun toString() = bitType.toString(bit)
}

fun leo(bit: Bit) = BitLeo(bit)
