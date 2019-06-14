package leo5.script

data class ScriptNonEmpty(val script: Script, val line: Line)

fun nonEmpty(script: Script, line: Line) = ScriptNonEmpty(script, line)
