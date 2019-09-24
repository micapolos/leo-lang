package leo13.pattern

import leo13.ObjectScripting
import leo13.itemName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class PatternItem : ObjectScripting() {
	override fun toString() = scriptingLine.toString()

	override val scriptingLine: ScriptLine
		get() = itemName lineTo when (this) {
			is LinePatternItem -> line.scriptingLine.rhs
			is RecursePatternItem -> script(recurse.scriptingLine)
		}
}

data class LinePatternItem(val line: PatternLine) : PatternItem()
data class RecursePatternItem(val recurse: Recurse) : PatternItem()

fun item(line: PatternLine): PatternItem = LinePatternItem(line)
fun item(recurse: Recurse): PatternItem = RecursePatternItem(recurse)
