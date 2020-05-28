package leo16

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

data class FunctionNativeDefinition(val function: Fn) : Definition() {
	override fun toString() = super.toString()
}

data class RepeatDefinition(val repeat: Repeat) : Definition() {
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
			is FunctionNativeDefinition -> function.asValue
			is RepeatDefinition -> repeat.asField.value
		}

val Constant.definition: Definition get() = ConstantDefinition(this)
val Function.definition: Definition get() = FunctionDefinition(this)
val Macro.definition: Definition get() = MacroDefinition(this)
val Repeat.definition: Definition get() = RepeatDefinition(this)
val Fn.definition: Definition get() = FunctionNativeDefinition(this)

inline fun Definition.apply(evaluated: Evaluated): Evaluated? =
	when (this) {
		is ConstantDefinition -> constant.apply(evaluated.value)?.let { evaluated.set(it) }
		is FunctionDefinition -> function.apply(evaluated.value)?.let { evaluated.set(it) }
		is MacroDefinition -> macro.apply(evaluated)
		is FunctionNativeDefinition -> function.apply(evaluated.value)?.let { evaluated.set(it) }
		is RepeatDefinition -> repeat.apply(evaluated.value)?.let { evaluated.set(it) }
	}

val Definition.patternValueOrNull: Value?
	get() =
		when (this) {
			is ConstantDefinition -> constant.key
			is FunctionDefinition -> function.patternValue
			is MacroDefinition -> macro.patternValue
			is FunctionNativeDefinition -> function.patternValue
			is RepeatDefinition -> null
		}

fun Value.does(apply: Value.() -> Value) =
	fn(apply).definition

val Field.parameterDefinition: Definition
	get() =
		selectWord().value.is_(value).definition

val Value.contentParameterDefinition: Definition
	get() =
		_content().value.is_(this).definition
