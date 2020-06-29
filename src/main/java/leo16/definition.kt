package leo16

import leo13.Stack
import leo13.StackLink
import leo13.asStack
import leo13.linkTo
import leo13.onlyStack
import leo13.stack
import leo16.names.*

sealed class Definition {
	override fun toString() = asSentence.toString()
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

val Definition.asSentence: Sentence
	get() =
		_definition(asValue)

val Definition.asValue: Value
	get() =
		when (this) {
			is ConstantDefinition -> constant.asValue
			is FunctionDefinition -> function.asValue
			is MacroDefinition -> macro.asValue
			is FunctionNativeDefinition -> function.asValue
			is RepeatDefinition -> repeat.asSentence.rhsValue
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

val Sentence.parameterDefinition: Definition
	get() =
		value(word()).is_(onlyValue).definition

val Value.thingParameterDefinition: Definition
	get() =
		value(_thing()).is_(this).definition

val Value.parameterDefinitionStack: Stack<Definition>
	get() =
		when (this) {
			EmptyValue -> stack()
			is LinkValue -> link.parameterDefinitionStackLink.asStack
			is NativeValue -> value(_native()).is_(this).definition.onlyStack
			is FunctionValue -> value(_function()).is_(this).definition.onlyStack
			is LazyValue -> value(_lazy()).is_(this).definition.onlyStack
			is FuncValue -> TODO()
		}

val ValueLink.parameterDefinitionStackLink: StackLink<Definition>
	get() =
		previousValue.parameterDefinitionStack.linkTo(lastSentence.parameterDefinition)
