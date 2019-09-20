package leo13.atom

import leo13.ObjectScripting
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

sealed class Op : ObjectScripting() {
	override val scriptingLine get() = "op" lineTo script(scriptingOpLine)
	abstract val scriptingOpLine: ScriptLine
}

data class AtomOp(val atom: Atom) : Op() {
	override fun toString() = super.toString()
	override val scriptingOpLine get() = atom.scriptingAtomLine
}
