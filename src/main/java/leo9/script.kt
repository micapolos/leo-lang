package leo9

data class Script(
	val lineStack: Stack<ScriptLine>)

data class ScriptLine(
	val name: String,
	val script: Script)

val Stack<ScriptLine>.script get() = Script(this)
fun script(vararg lines: ScriptLine) = stack(*lines).script
fun Script.push(line: ScriptLine) = lineStack.push(line).script
infix fun String.lineTo(script: Script) = ScriptLine(this, script)
val Script.name get() = lineStack.top.name
val Script.lhs get() = lineStack.pop.script
val Script.rhs get() = lineStack.top.script
