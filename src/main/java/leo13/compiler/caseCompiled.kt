package leo13.compiler

import leo13.ObjectScripting
import leo13.compiledName
import leo13.expression.Case
import leo13.expression.scriptLine
import leo13.pattern.Pattern
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class CaseCompiled(val case: Case, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = compiledName lineTo script(case.scriptLine, pattern.scriptingLine)
}

fun compiled(case: Case, pattern: Pattern) =
	CaseCompiled(case, pattern)
