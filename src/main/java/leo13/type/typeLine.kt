package leo13.type

import leo13.script.ScriptLine
import leo13.script.Scriptable
import leo13.script.lineTo
import leo13.script.script

data class TypeLine(val name: String, val rhs: TypeThunk) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = name
	override val scriptableBody get() = rhs.scriptableBody
	fun scriptableLineWithMeta(meta: Boolean): ScriptLine =
		if (meta) "meta" lineTo script(scriptableLine)
		else scriptableLine
}

infix fun String.lineTo(rhs: TypeThunk) = TypeLine(this, rhs)
infix fun String.lineTo(rhs: Type) = lineTo(thunk(rhs))

fun TypeLine.contains(line: TypeLine): Boolean =
	name == line.name && rhs.contains(line.rhs)

val ScriptLine.unsafeTypeLine: TypeLine
	get() =
		name lineTo rhs.unsafeType

