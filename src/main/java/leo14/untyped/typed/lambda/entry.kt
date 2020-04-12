package leo14.untyped.typed.lambda

import leo14.ScriptLine
import leo14.lineTo
import leo14.script
import leo14.untyped.leoString
import leo14.untyped.typed.Type

data class Entry(val type: Type, val typed: Typed) {
	override fun toString() = reflectScriptLine.leoString
}

infix fun Type.entryTo(typed: Typed) = Entry(this, typed)

val Entry.reflectScriptLine: ScriptLine
	get() =
		"entry" lineTo script(
			type.reflectScriptLine,
			typed.reflectScriptLine)