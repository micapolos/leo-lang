package leo5.script

data class ScriptNonEmpty(val line: Line, val script: Script)

fun nonEmpty(line: Line, script: Script) = ScriptNonEmpty(line, script)
