package leo21.definition

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lineTo
import leo14.plus
import leo14.script
import leo21.compiled.Compiled
import leo21.token.body.constantBinding
import leo21.token.strings.typeKeyword
import leo21.type.Type
import leo21.type.printScript

data class DefinitionConstant(val type: Type, val compiled: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "constant" lineTo script(type.reflectScriptLine, compiled.reflectScriptLine)
}

infix fun Type.is_(compiled: Compiled) = DefinitionConstant(this, compiled)

val DefinitionConstant.term get() = compiled.term
val DefinitionConstant.binding get() = type.constantBinding(compiled.type)

val DefinitionConstant.printScriptLine: ScriptLine
	get() =
		"constant".typeKeyword lineTo type.printScript
			.plus("is".typeKeyword lineTo compiled.type.printScript)

