package leo13.base

import leo13.base.type.Type
import leo13.base.type.case
import leo13.base.type.toString
import leo13.base.type.type
import leo13.script.lineTo
import leo13.script.script

val boolType: Type<Boolean> =
	type(
		"boolean",
		case(type("true")) { true },
		case(type("false")) { false })
	{
		(if (this) "true" else "false") lineTo script()
	}

data class Bool(val boolean: Boolean) {
	override fun toString() = boolType.toString(boolean)
}

fun bool(boolean: Boolean) = Bool(boolean)