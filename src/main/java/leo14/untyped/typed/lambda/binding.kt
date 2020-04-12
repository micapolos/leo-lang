package leo14.untyped.typed.lambda

import leo14.Script
import leo14.ScriptLine
import leo14.lambda2.fn
import leo14.lineTo
import leo14.script
import leo14.untyped.leoString
import leo14.untyped.typed.Type
import leo14.untyped.typed.functionTo
import leo14.untyped.typed.staticType
import leo14.untyped.typed.type

data class BindingConstant(val type: Type, val typed: Typed)
data class BindingFunction(val type: Type, val typed: Typed)

sealed class Binding {
	override fun toString() = reflectScriptLine.leoString
}

data class ConstantBinding(val constant: BindingConstant) : Binding() {
	override fun toString() = super.toString()
}

data class FunctionBinding(val function: BindingFunction) : Binding() {
	override fun toString() = super.toString()
}

infix fun Script.bindingTo(typed: Typed): Binding = ConstantBinding(BindingConstant(staticType, typed))
infix fun Type.bindingTo(typed: Typed): Binding = FunctionBinding(BindingFunction(this, typed))

val BindingConstant.reflectScriptLine
	get() =
		"constant" lineTo script(type.reflectScriptLine, typed.reflectScriptLine)

val BindingFunction.reflectScriptLine
	get() =
		"function" lineTo script(type.reflectScriptLine, typed.reflectScriptLine)

val Binding.reflectScriptLine: ScriptLine
	get() =
		"binding" lineTo script(
			when (this) {
				is ConstantBinding -> constant.reflectScriptLine
				is FunctionBinding -> function.reflectScriptLine
			})

val Binding.typed: Typed
	get() =
		when (this) {
			is ConstantBinding -> constant.typed
			is FunctionBinding -> function.type.functionTo(function.typed.type).type.typed(fn(function.typed.term))
		}

val Binding.type: Type
	get() =
		when (this) {
			is ConstantBinding -> constant.type
			is FunctionBinding -> function.type
		}