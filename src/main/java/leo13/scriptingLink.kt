package leo13

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class ScriptingLink<A : Scripting, B : Scripting>(val from: A, val to: B) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() = linkName lineTo script(
			fromName lineTo script(from.scriptingLine),
			toName lineTo script(to.scriptingLine))
}

infix fun <A : Scripting, B : Scripting> A.scriptingLinkTo(to: B) = ScriptingLink(this, to)