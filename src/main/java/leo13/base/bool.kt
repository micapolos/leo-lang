package leo13.base

import leo13.script.lineTo
import leo13.script.script
import leo13.scripter.Scripter
import leo13.scripter.case
import leo13.scripter.scripter
import leo13.scripter.toString

val boolType: Scripter<Boolean> =
	scripter(
		"boolean",
		case(scripter("true")) { true },
		case(scripter("false")) { false })
	{
		(if (this) "true" else "false") lineTo script()
	}

data class Bool(val boolean: Boolean) {
	override fun toString() = boolType.toString(boolean)
}

fun bool(boolean: Boolean) = Bool(boolean)