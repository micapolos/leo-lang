package leo13.script

data class ScriptLink(val lhs: Script, val line: ScriptLine) {
	override fun toString() = script.toString()
}

