package leo5

data class ScriptLine(val name: String, val script: Script)

fun line(name: String, script: Script) = ScriptLine(name, script)
