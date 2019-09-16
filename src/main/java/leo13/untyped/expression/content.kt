package leo13.untyped.expression

import leo13.script.lineTo
import leo13.script.script
import leo13.untyped.contentName
import leo13.untyped.scriptName

object Content

val content = Content

val Content.scriptLine
	get() =
		scriptName lineTo script(contentName)