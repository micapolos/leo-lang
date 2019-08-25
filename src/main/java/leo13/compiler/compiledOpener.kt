package leo13.compiler

import leo13.AsScriptLine
import leo13.Opening
import leo13.lineTo
import leo13.script

data class CompiledOpener(
	val lhs: Metable,
	val opening: Opening) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine
		get() = "opener" lineTo script(lhs.asScriptLine, opening.asScriptLine)
}

fun opener(metable: Metable, opening: Opening) = metable openerTo opening
infix fun Metable.openerTo(opening: Opening) = CompiledOpener(this, opening)
