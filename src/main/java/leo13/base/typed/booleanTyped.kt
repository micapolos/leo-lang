package leo13.base.typed

import leo13.base.Typed
import leo13.base.type.Type
import leo13.base.type.case
import leo13.base.type.type
import leo13.script.lineTo
import leo13.script.script

val booleanType: Type<Boolean> =
	type(
		"boolean",
		case(type("true")) { true },
		case(type("false")) { false })
	{
		(if (this) "true" else "false") lineTo script()
	}

data class BooleanTyped(val boolean: Boolean) : Typed<BooleanTyped>() {
	override fun toString() = super.toString()
	override val type = type(booleanType, { boolean }, { typed(this) })
}

fun typed(boolean: Boolean) = BooleanTyped(boolean)