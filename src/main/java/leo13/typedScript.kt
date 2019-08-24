package leo13

import leo.base.ifOrNull

data class TypedScript(val script: Script, val type: Type) {
	override fun toString() = asScript.toString()
}

data class TypedScriptLine(val name: String, val rhs: TypedScript)
data class TypedScriptLink(val lhs: TypedScript, val line: TypedScriptLine)

infix fun Script.of(type: Type) = TypedScript(this, type)
infix fun String.lineTo(rhs: TypedScript) = TypedScriptLine(this, rhs)
fun TypedScript.linkTo(line: TypedScriptLine) = TypedScriptLink(this, line)

val TypedScript.asScript get() = script.plus("of" lineTo type.asScript)

fun TypedScript.plus(line: TypedScriptLine) =
	script.plus(line.name lineTo line.rhs.script) of type.plus(line.name lineTo line.rhs.type)

val ScriptLink.parseTypedScript
	get() =
		ifOrNull(line.name == "of") {
			lhs of line.rhs.type
		}
