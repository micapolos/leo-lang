package leo5

data class ScriptApplication(val script: Script, val line: ScriptLine)

fun application(script: Script, line: ScriptLine) = ScriptApplication(script, line)
fun ScriptApplication.invoke(script: Script) = script.plus(line(line.name, value(line.script)))