package leo13.type

import leo.base.ifOrNull
import leo13.script.*

data class Either(val name: String, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "either"
	override val scriptableBody get() = script(name lineTo type.scriptableBody)
}

infix fun String.eitherTo(type: Type) = Either(this, type)
fun either(name: String, type: Type = leo13.type.type()) = Either(name, type)

val Either.asFirstScriptLine get() = name lineTo type.scriptableBody
val Either.asNextScriptLine get() = "or" lineTo script(asFirstScriptLine)

val TypeLine.either
	get() =
		name eitherTo rhs

val ScriptLine.eitherOrNull: Either?
	get() =
		ifOrNull(name == "either") {
			rhs.onlyLineOrNull?.let { onlyLine ->
				onlyLine.rhs.typeOrNull?.let { type ->
					either(onlyLine.name, type)
				}
			}
		}

fun Either.matches(scriptLine: ScriptLine): Boolean =
	name == scriptLine.name && type.matches(scriptLine.rhs)

fun Either.contains(line: TypeLine) =
	name == line.name && type == line.rhs
