package leo21.definition

import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.fn
import leo14.lineTo
import leo14.plus
import leo14.script
import leo21.compiled.Compiled
import leo21.token.body.functionBinding
import leo21.token.strings.typeKeyword
import leo21.type.Type
import leo21.type.printScript

data class DefinitionFunction(val type: Type, val compiled: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "function" lineTo script(type.reflectScriptLine, compiled.reflectScriptLine)
}

infix fun Type.does(compiled: Compiled) = DefinitionFunction(this, compiled)

val DefinitionFunction.term get() = compiled.term
val DefinitionFunction.binding get() = type.functionBinding(compiled.type)

val DefinitionFunction.printScriptLine: ScriptLine
	get() =
		"function".typeKeyword lineTo type.printScript
			.plus("does".typeKeyword lineTo compiled.type.printScript)

