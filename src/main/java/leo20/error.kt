package leo20

import leo14.ScriptLine
import leo14.lineTo
import leo14.script

fun error(vararg lines: ScriptLine): Nothing =
	error(script("error" lineTo script(*lines)).toString())
