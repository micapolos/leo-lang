package leo13.compiler

import leo.base.fold
import leo13.*
import leo13.script.*
import leo13.script.Function

data class Context(
	val types: Types,
	val functions: Functions,
	val typeBindings: TypeBindings) : AsScriptLine() {
	override fun toString() = super.toString()
	override val asScriptLine = "context" lineTo script(
		types.asScriptLine,
		functions.asScriptLine,
		typeBindings.asScriptLine)
}

fun context() = Context(types(), functions(), typeBindings())
fun context(types: Types, functions: Functions, typeBindings: TypeBindings) = Context(types, functions, typeBindings)
fun Context.bind(type: Type) = copy(typeBindings = typeBindings.push(type))
fun Context.plus(type: Type) = copy(types = types.plus(type))
fun Context.plus(function: Function) = copy(functions = functions.plus(function))

fun Context.typedOrNull(script: Script): Typed? =
	metable(false, compiled(this, typed()))
		.head
		.compiler
		.fold(script.tokenSeq) { push(it) }
		.successHeadOrNull
		?.completedCompiledOrNull
		?.typed
