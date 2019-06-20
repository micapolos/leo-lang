package leo5.value

data class ScriptLink(val script: Script, val line: Line)
infix fun Script.linkTo(line: Line) = ScriptLink(this, line)
