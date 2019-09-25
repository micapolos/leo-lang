package leo13

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

data class Option<V : Scripting>(val nullLine: ScriptLine, val valueOrNull: V?) : ObjectScripting() {
	override fun toString() = super.toString()

	override val scriptingLine: ScriptLine
		get() =
			optionName lineTo script(valueOrNull?.scriptingLine ?: nullLine)
}