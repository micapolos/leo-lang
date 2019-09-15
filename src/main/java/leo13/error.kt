package leo13

import leo13.script.lineTo
import leo13.script.script

object Error : ObjectScripting() {
	override fun toString() = scriptingLine.toString()
	override val scriptingLine get() = "error" lineTo script()
}

val error = Error