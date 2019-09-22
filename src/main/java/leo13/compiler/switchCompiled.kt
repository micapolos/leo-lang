package leo13.compiler

import leo13.*
import leo13.expression.Switch
import leo13.expression.plus
import leo13.expression.scriptLine
import leo13.pattern.Pattern
import leo13.script.lineTo
import leo13.script.script

data class SwitchCompiled(val switch: Switch, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			compiledName lineTo script(switch.scriptLine, pattern.scriptingLine)
}

fun compiled(switch: Switch, pattern: Pattern) =
	SwitchCompiled(switch, pattern)

fun SwitchCompiled.plus(compiled: CaseCompiled) =
	compiled(switch.plus(compiled.case), compiled.pattern).let { compiled ->
		if (!switch.caseStack.isEmpty && pattern != compiled.pattern)
			tracedError(mismatchName lineTo script(
				expectedName lineTo script(pattern.scriptingLine),
				actualName lineTo script(compiled.pattern.scriptingLine)))
		else compiled
	}
