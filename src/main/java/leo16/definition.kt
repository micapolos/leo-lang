package leo16

import leo13.map
import leo13.reverse
import leo16.names.*

sealed class Definition {
	override fun toString() = asField.toString()
}

data class ConstantDefinition(val constant: Constant) : Definition() {
	override fun toString() = super.toString()
}

data class FunctionDefinition(val function: Function) : Definition() {
	override fun toString() = super.toString()
}

data class MacroDefinition(val macro: Macro) : Definition() {
	override fun toString() = super.toString()
}

data class FnDefinition(val fn: Fn) : Definition() {
	override fun toString() = super.toString()
}

data class RecurseDefinition(val recurse: Recurse) : Definition() {
	override fun toString() = super.toString()
}

val Definition.asField: Field
	get() =
		_definition(asValue)

val Definition.asValue: Value
	get() =
		when (this) {
			is ConstantDefinition -> constant.asValue
			is FunctionDefinition -> function.asValue
			is MacroDefinition -> macro.asValue
			is FnDefinition -> fn.nativeString().value
			is RecurseDefinition -> recurse.asField.value
		}

val Constant.definition: Definition get() = ConstantDefinition(this)
val Function.definition: Definition get() = FunctionDefinition(this)
val Macro.definition: Definition get() = MacroDefinition(this)
val Recurse.definition: Definition get() = RecurseDefinition(this)
val Fn.definition: Definition get() = FnDefinition(this)

fun Definition.apply(evaluated: Evaluated): Evaluated? =
	when (this) {
		is ConstantDefinition -> constant.apply(evaluated.value)?.let { evaluated.set(it) }
		is FunctionDefinition -> function.apply(evaluated.value)?.let { evaluated.set(it) }
		is MacroDefinition -> macro.apply(evaluated)
		is FnDefinition -> fn.apply(evaluated.value)?.let { evaluated.set(it) }
		is RecurseDefinition -> recurse.apply(evaluated.value).let { evaluated.set(it) }
	}

val Value.parameterDictionary: Dictionary
	get() =
		fieldStack.map { parameterDefinition }.dictionary

fun Value.gives(apply: Value.() -> Value) =
	pattern.fn(apply).definition

val Field.parameterDefinition: Definition
	get() =
		selectWord.pattern.is_(value).definition

val Match.anyParameterDefinitionOrNull: Definition?
	get() =
		anyFieldStackOrNull?.let { fieldStack ->
			_the(fieldStack.reverse.value).parameterDefinition
		}
