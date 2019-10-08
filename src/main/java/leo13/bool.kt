package leo13

import leo13.script.lineTo
import leo13.script.script

inline class BooleanLeo(val boolean: Boolean) : Scripting {
	override fun toString() = scriptingLine.toString()
	override val scriptingLine get() = booleanName lineTo script("$boolean")
	operator fun not() = leo(!boolean)
	inline fun and(otherFn: () -> BooleanLeo) = leo(boolean && otherFn().boolean)
	inline fun or(otherFn: () -> BooleanLeo) = leo(boolean || otherFn().boolean)
	infix fun and(other: BooleanLeo) = and { other }
	infix fun or(other: BooleanLeo) = or { other }
	infix fun xor(other: BooleanLeo) = leo(boolean xor other.boolean)
}

fun leo(boolean: Boolean) = BooleanLeo(boolean)
val leoTrue = leo(true)
val leoFalse = leo(false)

val x = leoTrue.and { leoFalse }.and { leoTrue and leoFalse }
