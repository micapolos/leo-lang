package leo13.script.core

import leo.binary.*
import leo13.LeoObject
import leo13.script.Script
import leo13.script.lineTo
import leo13.script.script

data class BitLeo(val bit: Bit) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "bit"
	override val scriptableBody get() = script(if (bit.isZero) "zero" else "one")
}

fun leo(bit: Bit) = BitLeo(bit)

data class ByteLeo(val byte: Byte) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "byte"
	override val scriptableBody: Script
		get() = script(
			"first" lineTo script(leo(byte.bit7).scriptableLine),
			"second" lineTo script(leo(byte.bit6).scriptableLine),
			"third" lineTo script(leo(byte.bit5).scriptableLine),
			"fourth" lineTo script(leo(byte.bit4).scriptableLine),
			"fifth" lineTo script(leo(byte.bit3).scriptableLine),
			"sixth" lineTo script(leo(byte.bit2).scriptableLine),
			"seventh" lineTo script(leo(byte.bit1).scriptableLine),
			"eight" lineTo script(leo(byte.bit0).scriptableLine))
}

fun leo(byte: Byte) = ByteLeo(byte)

data class StringLeo(val string: String) : LeoObject() {
	override fun toString() = super.toString()
	override val scriptableName get() = "string"
	override val scriptableBody get() = script()
}