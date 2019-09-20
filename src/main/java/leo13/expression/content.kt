package leo13.expression

import leo13.contentName
import leo13.script.lineTo
import leo13.script.script
import leo13.scriptName

object Content

val content = Content

val Content.scriptLine
	get() =
		scriptName lineTo script(contentName)