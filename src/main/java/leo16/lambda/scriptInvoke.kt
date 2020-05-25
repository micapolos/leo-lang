package leo16.lambda

import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.script

operator fun String.invoke(vararg lines: ScriptLine) = lineTo(script(*lines))
operator fun String.invoke(script: Script) = lineTo(script)
