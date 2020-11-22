package leo21.token.body

import leo.base.updateIfNotNull
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.Term
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo14.lineTo
import leo14.plus
import leo14.script
import leo21.compiled.Compiled
import leo21.compiled.of
import leo21.prim.Prim
import leo21.prim.runtime.value
import leo21.token.strings.typeKeyword
import leo21.type.Line
import leo21.type.Type
import leo21.type.printScript

sealed class Definition : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = "definition" lineTo script(
			when (this) {
				is ConstantDefinition -> constant.reflectScriptLine
				is FunctionDefinition -> function.reflectScriptLine
				is TypeDefinition -> type.reflectScriptLine
			}
		)
}

data class ConstantDefinition(val constant: DefinitionConstant) : Definition() {
	override fun toString() = super.toString()
}

data class FunctionDefinition(val function: DefinitionFunction) : Definition() {
	override fun toString() = super.toString()
}

data class TypeDefinition(val type: DefinitionType) : Definition() {
	override fun toString() = super.toString()
}

data class DefinitionFunction(val type: Type, val compiled: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "function" lineTo script(type.reflectScriptLine, compiled.reflectScriptLine)
}

data class DefinitionConstant(val type: Type, val compiled: Compiled) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "constant" lineTo script(type.reflectScriptLine, compiled.reflectScriptLine)
}

data class DefinitionType(val line: Line) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = "type" lineTo script(line.reflectScriptLine)
}

fun constantDefinition(type: Type, compiled: Compiled): Definition =
	ConstantDefinition(DefinitionConstant(type, compiled))

fun functionDefinition(type: Type, compiled: Compiled): Definition =
	FunctionDefinition(DefinitionFunction(type, compiled))

fun typeDefinition(line: Line): Definition =
	TypeDefinition(DefinitionType(line))

fun Compiled.wrap(definition: Definition): Compiled =
	updateIfNotNull(definition.termOrNull) {
		fn(term).invoke(it).of(type)
	}

val Definition.termOrNull: Term<Prim>?
	get() =
		when (this) {
			is ConstantDefinition -> constant.compiled.term
			is FunctionDefinition -> fn(function.compiled.term)
			is TypeDefinition -> null
		}

val Definition.bindingOrNull: Binding?
	get() =
		when (this) {
			is ConstantDefinition -> constant.type.constantBinding(constant.compiled.type)
			is FunctionDefinition -> function.type.functionBinding(function.compiled.type)
			is TypeDefinition -> null
		}

val Definition.lineOrNull: Line?
	get() =
		when (this) {
			is ConstantDefinition -> null
			is FunctionDefinition -> null
			is TypeDefinition -> type.line
		}

val Definition.printScriptLine: ScriptLine
	get() =
		when (this) {
			is ConstantDefinition ->
				"constant" lineTo constant.type.printScript
					.plus("is".typeKeyword lineTo constant.compiled.type.printScript)
			is FunctionDefinition ->
				"function" lineTo function.type.printScript
					.plus("does".typeKeyword lineTo function.compiled.type.printScript)
			is TypeDefinition ->
				"type" lineTo script(type.line.printScriptLine)
		}

val Definition.valueOrNull: Value<Prim>?
	get() =
		termOrNull?.value
