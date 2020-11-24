package leo21.definition

import leo.base.updateIfNotNull
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.Term
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.value.Scope
import leo14.lambda.value.Value
import leo14.lambda.value.function
import leo14.lambda.value.value
import leo14.lineTo
import leo14.script
import leo21.compiled.Compiled
import leo21.compiled.of
import leo21.prim.Prim
import leo21.prim.runtime.value
import leo21.token.body.Binding
import leo21.type.Line
import leo21.type.Type

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

fun definition(function: DefinitionFunction): Definition = FunctionDefinition(function)
fun definition(constant: DefinitionConstant): Definition = ConstantDefinition(constant)
fun definition(type: DefinitionType): Definition = TypeDefinition(type)

fun constantDefinition(type: Type, compiled: Compiled): Definition =
	definition(type is_ compiled)

fun functionDefinition(type: Type, compiled: Compiled): Definition =
	definition(type does compiled)

fun typeDefinition(line: Line): Definition =
	TypeDefinition(DefinitionType(line))

fun Compiled.wrap(definition: Definition): Compiled =
	updateIfNotNull(definition.termOrNull) {
		fn(term).invoke(it).of(type)
	}

val Definition.termOrNull: Term<Prim>?
	get() =
		when (this) {
			is ConstantDefinition -> constant.term
			is FunctionDefinition -> fn(function.term)
			is TypeDefinition -> null
		}

fun Scope<Prim>.valueOrNull(definition: Definition): Value<Prim>? =
	when (definition) {
		is ConstantDefinition -> definition.constant.term.value
		is FunctionDefinition -> function(definition.function.term).value
		is TypeDefinition -> null
	}

val Definition.bindingOrNull: Binding?
	get() =
		when (this) {
			is ConstantDefinition -> constant.binding
			is FunctionDefinition -> function.binding
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
			is ConstantDefinition -> constant.printScriptLine
			is FunctionDefinition -> function.printScriptLine
			is TypeDefinition -> type.printScriptLine
		}

