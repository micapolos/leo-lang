package leo13.base

import leo13.base.type.*

val trueType: Type<Boolean> = type("true", body(struct(true)))
val falseType: Type<Boolean> = type("false", body(struct(false)))

val booleanType: Type<Boolean> =
	type(
		"boolean",
		body(
			choice(
				case(trueType) { true },
				case(falseType) { false }) {
				if (this) trueType to true
				else falseType to false
			}))

data class BooleanTyped(val boolean: Boolean) : Typed<Boolean>() {
	override fun toString() = super.toString()
	override val type = booleanType
}

fun typed(boolean: Boolean) = BooleanTyped(boolean)