package leo13.token

import leo13.script.lineTo
import leo13.script.script

object Closing {
	override fun toString() = asScriptLine.toString()
	val asScriptLine get() = "closing" lineTo script()
}

val closing = Closing
