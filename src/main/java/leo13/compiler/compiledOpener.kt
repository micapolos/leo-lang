package leo13.compiler

import leo13.Opening
import leo13.Scriptable
import leo13.script

data class CompiledOpener(
	val lhs: Metable,
	val opening: Opening) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "opener"
	override val scriptableBody get() = script(lhs.scriptableLine, opening.asScriptLine)
}

fun opener(metable: Metable, opening: Opening) = metable openerTo opening
infix fun Metable.openerTo(opening: Opening) = CompiledOpener(this, opening)
