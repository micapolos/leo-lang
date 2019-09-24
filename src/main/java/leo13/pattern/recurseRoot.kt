package leo13.pattern

import leo13.ObjectScripting
import leo13.rootName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class RecurseRoot(val recurse: Recurse, val node: PatternNode) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = rootName lineTo script(recurse.scriptingLine, node.scriptingLine)

	val recurseIncrease get() = RecurseRoot(recurse.increase, node)
}

fun root(recurse: Recurse, node: PatternNode) = RecurseRoot(recurse, node)

fun RecurseRoot?.orNullRecurseIncrease(node: PatternNode): RecurseRoot =
	this?.recurseIncrease ?: root(onceRecurse, node)