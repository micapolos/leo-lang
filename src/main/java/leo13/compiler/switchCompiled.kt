package leo13.compiler

import leo13.ObjectScripting
import leo13.expression.Switch
import leo13.expression.plus
import leo13.expression.scriptLine
import leo13.isEmpty
import leo13.pattern.Pattern
import leo13.script.lineTo
import leo13.script.script
import leo13.tracedError

data class SwitchCompiled(val switch: Switch, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			"compiled" lineTo script(switch.scriptLine, pattern.scriptingLine)
}

fun compiled(switch: Switch, pattern: Pattern) =
	SwitchCompiled(switch, pattern)

fun SwitchCompiled.plus(compiled: CaseCompiled) =
	compiled(switch.plus(compiled.case), compiled.pattern).let { compiled ->
		if (!switch.caseStack.isEmpty && pattern != compiled.pattern)
			tracedError("mismatch" lineTo script("pattern")) // TODO: Better message
		else compiled
	}
