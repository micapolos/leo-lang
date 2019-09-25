package leo13.type

import leo.base.orIfNull
import leo13.ObjectScripting
import leo13.recurseName
import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class Recurse(val lhsOrNull: Recurse?) : ObjectScripting() {
	override fun toString() = super.toString()
	override val scriptingLine: ScriptLine
		get() = recurseName lineTo lhsOrNull?.scriptingLine?.let { script(it) }.orIfNull { script() }
}

val onceRecurse = Recurse(null)
val Recurse?.increase get() = Recurse(this)
