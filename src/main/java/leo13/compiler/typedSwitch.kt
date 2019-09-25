package leo13.compiler

import leo13.*
import leo13.expression.Switch
import leo13.expression.plus
import leo13.expression.scriptLine
import leo13.type.Type
import leo13.script.lineTo
import leo13.script.script

data class TypedSwitch(val switch: Switch, val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			typedName lineTo script(switch.scriptLine, type.scriptingLine)
}

fun typed(switch: Switch, type: Type) =
	TypedSwitch(switch, type)

fun TypedSwitch.plus(typed: TypedCase) =
	typed(switch.plus(typed.case), typed.type).let { typed ->
		if (!switch.caseStack.isEmpty && type != typed.type)
			tracedError(mismatchName lineTo script(
				expectedName lineTo script(type.scriptingLine),
				actualName lineTo script(typed.type.scriptingLine)))
		else typed
	}
