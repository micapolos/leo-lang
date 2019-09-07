package leo13.base.typed

import leo.binary.*
import leo13.base.Typed
import leo13.base.type.Type
import leo13.base.type.case
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

data class BitTyped(val bit: Bit) : Typed<Bit>() {
	override fun toString() = super.toString()
	override val type = bitType
}

fun typed(bit: Bit) = BitTyped(bit)
