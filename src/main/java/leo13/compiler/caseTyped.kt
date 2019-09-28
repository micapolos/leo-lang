package leo13.compiler

import leo13.ObjectScripting
import leo13.expression.Case
import leo13.expression.scriptLine
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.type.Type
import leo13.typedName

data class CaseTyped(val case: Case, val type: Type) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = typedName lineTo script(case.scriptLine, type.scriptingLine)
}

fun typed(case: Case, type: Type) =
	CaseTyped(case, type)
