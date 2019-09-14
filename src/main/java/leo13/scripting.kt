package leo13

import leo13.script.ScriptLine
import leo13.script.lineTo
import leo13.script.script

interface Scripting {
	val scriptingLine: ScriptLine
}

abstract class ObjectScripting : Scripting {
	override fun toString() = scriptingLine.toString()
}

val unitScripting = object : Scripting {
	override val scriptingLine get() = "unit" lineTo script()
}
