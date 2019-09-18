package leo13.untyped.atom

import leo13.ObjectScripting
import leo13.script.lineTo
import leo13.script.script

data class Function(val given: Atom, val op: Op) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			"function" lineTo script(
				"given" lineTo script(given.scriptingAtomLine),
				op.scriptingLine)
}