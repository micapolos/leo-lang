package leo13.compiler

import leo.base.fold
import leo13.Script
import leo13.Scriptable
import leo13.script
import leo13.script.*
import leo13.script.Function
import leo13.tokenSeq
import leo13.type.*

data class Context(
	val types: Types,
	val functions: Functions,
	val typeBindings: TypeBindings) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "context"
	override val scriptableBody
		get() = script(
			types.scriptableLine,
			functions.scriptableLine,
			typeBindings.scriptableLine)
}

fun context() = Context(leo13.type.types(), functions(), typeBindings())
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
