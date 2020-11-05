package leo21.dictionary

import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.fn
import leo14.lineTo
import leo14.matchInfix
import leo14.script
import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.of
import leo21.compiler.Binding
import leo21.compiler.Bindings
import leo21.compiler.constantBinding
import leo21.compiler.functionBinding
import leo21.compiler.push
import leo21.compiler.compiled
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.type

sealed class Definition : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "definition" lineTo script(when (this) {
			is FunctionDefinition -> "function" lineTo script(
				"key" lineTo script(arrowCompiled.arrow.lhs.reflectScriptLine),
				"value" lineTo script((arrowCompiled.term of arrowCompiled.arrow.rhs).reflectScriptLine))
			is ConstantDefinition -> "constant" lineTo script(
				"key" lineTo script(type.reflectScriptLine),
				"value" lineTo script(compiled.reflectScriptLine))
		})
}

data class FunctionDefinition(val arrowCompiled: ArrowCompiled) : Definition() {
	override fun toString() = super.toString()
}

data class ConstantDefinition(val type: Type, val compiled: Compiled) : Definition() {
	override fun toString() = super.toString()
}

fun definition(arrowCompiled: ArrowCompiled): Definition = FunctionDefinition(arrowCompiled)
fun definition(type: Type, compiled: Compiled): Definition = ConstantDefinition(type, compiled)

val Definition.binding: Binding
	get() =
		when (this) {
			is FunctionDefinition -> functionBinding(arrowCompiled.arrow)
			is ConstantDefinition -> constantBinding(type arrowTo compiled.type)
		}

fun Bindings.definition(script: Script): Definition =
	null
		?: doesDefinitionOrNull(script)
		?: givesDefinitionOrNull(script)
		?: error("definition parse error")

fun Bindings.doesDefinitionOrNull(script: Script): Definition? =
	script.matchInfix("does") { lhs, rhs ->
		lhs.type.let { lhsType ->
			push(lhsType).compiled(rhs).let { typed ->
				definition(fn(typed.term).of(lhsType arrowTo typed.type))
			}
		}
	}

fun Bindings.givesDefinitionOrNull(script: Script): Definition? =
	script.matchInfix("gives") { lhs, rhs ->
		definition(lhs.type, compiled(rhs))
	}
