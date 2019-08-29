package leo13.type

import leo.base.ifOrNull
import leo13.script.Script
import leo13.script.ScriptLink
import leo13.script.lineTo
import leo13.script.plus

data class TypedScript(val script: Script, val type: Type) {
	override fun toString() = asScript.toString()
	val asScript get() = script.plus("of" lineTo type.scriptableBody)
}

data class TypedScriptLine(val name: String, val rhs: TypedScript)
data class TypedScriptLink(val lhs: TypedScript, val line: TypedScriptLine)

infix fun Script.of(type: Type) = TypedScript(this, type)
infix fun String.lineTo(rhs: TypedScript) = TypedScriptLine(this, rhs)
fun TypedScript.linkTo(line: TypedScriptLine) = TypedScriptLink(this, line)

fun TypedScript.plus(line: TypedScriptLine) =
	script.plus(line.name lineTo line.rhs.script) of type.plus(line.name lineTo line.rhs.type)

val ScriptLink.parseTypedScript
	get() =
		ifOrNull(line.name == "of") {
			line.rhs.unsafeType.let { type ->
				lhs of type
			}
		}
