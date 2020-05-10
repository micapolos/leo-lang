package leo16

import leo13.map
import leo13.reverse
import leo16.names.*

sealed class Definition {
	override fun toString() = asField.toString()
}

data class IsDefinition(val is_: Is) : Definition() {
	override fun toString() = super.toString()
}

data class GivesDefinition(val gives: Gives) : Definition() {
	override fun toString() = super.toString()
}

data class ExpandsDefinition(val expands: Expands) : Definition() {
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
			is IsDefinition -> is_.asValue
			is GivesDefinition -> gives.asValue
			is ExpandsDefinition -> expands.asValue
			is FnDefinition -> fn.nativeString().value
			is RecurseDefinition -> recurse.asField.value
		}

val Is.definition: Definition get() = IsDefinition(this)
val Gives.definition: Definition get() = GivesDefinition(this)
val Expands.definition: Definition get() = ExpandsDefinition(this)
val Recurse.definition: Definition get() = RecurseDefinition(this)
val Fn.definition: Definition get() = FnDefinition(this)

fun Definition.apply(evaluated: Evaluated): Evaluated? =
	when (this) {
		is IsDefinition -> is_.apply(evaluated.value)?.let { evaluated.set(it) }
		is GivesDefinition -> gives.apply(evaluated.value)?.let { evaluated.set(it) }
		is ExpandsDefinition -> expands.apply(evaluated)
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
