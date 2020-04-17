package leo15.type

import leo14.ScriptLine
import leo14.invoke
import leo14.lineTo
import leo14.script
import leo15.lambda.Term
import leo15.lambda.at
import leo15.lambda.invoke
import leo15.string

data class Binding(val key: BindingKey, val value: BindingValue) {
	override fun toString() = reflectScriptLine.string
}

data class BindingKey(val type: Type) {
	override fun toString() = reflectScriptLine.string
}

sealed class BindingValue {
	override fun toString() = reflectScriptLine.string
}

data class TypedBindingValue(val typed: Typed) : BindingValue() {
	override fun toString() = super.toString()
}

data class ScopedBindingValue(val scoped: Scoped) : BindingValue() {
	override fun toString() = super.toString()
}

val Type.key get() = BindingKey(this)
infix fun BindingKey.bindingTo(value: BindingValue) = Binding(this, value)
val Typed.value: BindingValue get() = TypedBindingValue(this)
val Scoped.value: BindingValue get() = ScopedBindingValue(this)

val Binding.typed: Typed
	get() =
		when (value) {
			is TypedBindingValue -> value.typed
			is ScopedBindingValue ->
				value.scoped.typed.expression of
					key.type
						.arrowTo(value.scoped.typed.type)
						.line
						.choice
						.type
		}

fun BindingValue.invoke(index: Int, term: Term): Typed? =
	when (this) {
		is TypedBindingValue -> at(index).dynamicExpression of typed.type
		is ScopedBindingValue -> at(index).invoke(term).dynamicExpression of scoped.typed.type
	}

val BindingKey.reflectScriptLine: ScriptLine
	get() =
		"key" lineTo type.script

val BindingValue.reflectScriptLine: ScriptLine
	get() =
		"value" lineTo script(
			when (this) {
				is TypedBindingValue -> typed.reflectScriptLine
				is ScopedBindingValue -> scoped.reflectScriptLine
			})

val Binding.reflectScriptLine: ScriptLine
	get() =
		"binding"(key.reflectScriptLine, value.reflectScriptLine)