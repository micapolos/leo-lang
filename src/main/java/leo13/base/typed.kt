package leo13.base

import leo13.base.type.Type
import leo13.base.type.scriptLine
import leo13.script.ScriptLine

abstract class Typed<V : Any> {
	abstract val type: Type<V>
	val typedScriptLine: ScriptLine get() = type.scriptLine(this as V)
	override fun toString() = type.scriptLine(this as V).toString()
}
