package leo13.atom

import leo13.ObjectScripting
import leo13.functionName
import leo13.givenName
import leo13.script.lineTo
import leo13.script.script

data class Function(val given: Atom, val op: Op) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine
		get() =
			functionName lineTo script(
				givenName lineTo script(given.scriptingAtomLine),
				op.scriptingLine)
}