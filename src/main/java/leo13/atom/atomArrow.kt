package leo13.atom

import leo13.ObjectScripting
import leo13.arrowName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script
import leo13.toName

data class AtomArrow(val lhs: Atom, val rhs: Atom) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = arrowName lineTo script(lhs.scriptingAtomLine).plus(toName lineTo script(rhs.scriptingAtomLine))
}