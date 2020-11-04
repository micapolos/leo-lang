package leo21.evaluated

import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.value.Value
import leo14.lambda.value.term
import leo14.lambda.value.value
import leo14.lineTo
import leo14.script
import leo21.prim.Prim
import leo21.prim.prim
import leo21.type.Type
import leo21.type.type
import leo21.typed.Typed

data class Evaluated(val value: Value<Prim>, val type: Type) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "evaluated" lineTo script(value.reflectScriptLine, type.reflectScriptLine)
}

infix fun Value<Prim>.of(type: Type) = Evaluated(this, type)

val emptyEvaluated = Evaluated(value(prim(0)), type())

val Evaluated.typed: Typed
	get() =
		Typed(value.term, type)

val Evaluated.script: Script
	get() =
		script(value, type)