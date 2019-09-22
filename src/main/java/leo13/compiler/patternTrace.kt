package leo13.compiler

import leo.base.orIfNull
import leo13.ObjectScripting
import leo13.pattern.Pattern
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.traceName

data class PatternTrace(val lhsOrNull: PatternTrace?, val pattern: Pattern) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = traceName lineTo
			lhsOrNull?.scriptingLine?.rhs.orIfNull { script() }.plus(pattern.scriptingLine)
}
