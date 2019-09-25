package leo13.compiler

import leo13.*
import leo13.expression.Switch
import leo13.expression.plus
import leo13.expression.scriptLine
import leo13.type.Type
import leo13.script.lineTo
import leo13.script.script

data class SwitchCompiled(val switch: Switch, val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			compiledName lineTo script(switch.scriptLine, type.scriptingLine)
}

fun compiled(switch: Switch, type: Type) =
	SwitchCompiled(switch, type)

fun SwitchCompiled.plus(compiled: CaseCompiled) =
	compiled(switch.plus(compiled.case), compiled.type).let { compiled ->
		if (!switch.caseStack.isEmpty && type != compiled.type)
			tracedError(mismatchName lineTo script(
				expectedName lineTo script(type.scriptingLine),
				actualName lineTo script(compiled.type.scriptingLine)))
		else compiled
	}
