package leo15

import leo14.Script
import leo14.ScriptLine
import leo14.lambda2.Term
import leo14.lambda2.at
import leo14.lambda2.invoke
import leo14.lineTo
import leo14.script
import leo14.untyped.leoString

data class Binding(val key: BindingKey, val value: BindingValue) {
	override fun toString() = reflectScriptLine.leoString
}

data class BindingKey(val type: Type)

sealed class BindingValue {
	override fun toString() = reflectScriptLine.leoString
}

data class TypedBindingValue(val typed: Typed) : BindingValue() {
	override fun toString() = super.toString()
}

data class CompiledBindingValue(val compiled: Compiled) : BindingValue() {
	override fun toString() = super.toString()
}

val Type.key: BindingKey get() = BindingKey(this)
infix fun Script.bindingTo(typed: Typed): Binding = Binding(staticType.key, TypedBindingValue(typed))
infix fun Type.bindingTo(compiled: Compiled): Binding = Binding(key, CompiledBindingValue(compiled))

val BindingKey.reflectScriptLine: ScriptLine
	get() =
		"key" lineTo script(type.reflectScriptLine)

val BindingValue.reflectScriptLine: ScriptLine
	get() =
		"value" lineTo script(
			when (this) {
				is TypedBindingValue -> typed.reflectScriptLine
				is CompiledBindingValue -> compiled.reflectScriptLine
			})

val Binding.reflectScriptLine: ScriptLine
	get() =
		"binding" lineTo script(
			key.reflectScriptLine,
			value.reflectScriptLine)

val Binding.typed: Typed
	get() =
		when (value) {
			is TypedBindingValue -> value.typed
			is CompiledBindingValue ->
				key.type
					.functionTo(value.compiled.typed.type)
					.type
					.typed(value.compiled.typed.term)
		}

fun BindingValue.invoke(index: Int, term: Term): Typed? =
	when (this) {
		is TypedBindingValue -> typed.type.typed(at(index))
		is CompiledBindingValue -> compiled.typed.type.typed(at(index).invoke(term))
	}
