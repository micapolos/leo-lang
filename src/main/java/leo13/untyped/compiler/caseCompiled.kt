package leo13.untyped.compiler

import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.expression.Case
import leo13.untyped.expression.scriptLine
import leo13.untyped.pattern.Pattern

data class CaseCompiled(val case: Case, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = "compiled" lineTo script(case.scriptLine, pattern.scriptingLine)
}

fun compiled(case: Case, pattern: Pattern) =
	CaseCompiled(case, pattern)
