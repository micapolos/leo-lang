package leo13.pattern

import leo.base.notNullOrError
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

	val unexpandedLineOrNull get() = (this as? LinePatternItem)?.unexpandedLine
	val recurseOrNull get() = (this as? RecursePatternItem)?.recurse
	val line get() = unexpandedLineOrNull!!.notNullOrError("recurse")

	fun expand(rootOrNull: RecurseRoot? = null): PatternItem =
		when (this) {
			is LinePatternItem -> item(line.expand(rootOrNull))
			is RecursePatternItem ->
				rootOrNull
					.notNullOrError("recurse")
					.let { root ->
						if (recurse == root.recurse) item(root.line)
						else this
					}
		}

	fun contains(item: PatternItem, traceOrNull: PatternTrace?): Boolean =
		when (this) {
			is LinePatternItem ->
				when (item) {
					is LinePatternItem -> line.contains(item.line, traceOrNull)
					is RecursePatternItem -> false
				}
			is RecursePatternItem ->
				when (item) {
					is LinePatternItem -> traceOrNull.orNullPlus(item.line).run { line.contains(item.line, this) }
					is RecursePatternItem -> recurse == item.recurse
				}
		}
}

data class LinePatternItem(val unexpandedLine: PatternLine) : PatternItem()
data class RecursePatternItem(val recurse: Recurse) : PatternItem()

fun item(line: PatternLine): PatternItem = LinePatternItem(line)
fun item(recurse: Recurse): PatternItem = RecursePatternItem(recurse)
