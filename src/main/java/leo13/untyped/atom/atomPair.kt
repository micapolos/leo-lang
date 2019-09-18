package leo13.untyped.atom

import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.plus
import leo13.script.script

data class AtomPair(val lhs: Atom, val rhs: Atom) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = "pair" lineTo script(lhs.scriptingAtomLine).plus("to" lineTo script(rhs.scriptingAtomLine))
}