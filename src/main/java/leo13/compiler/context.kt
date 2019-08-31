package leo13.compiler

import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.script
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

fun context() = Context(types(), functions(), typeBindings())
fun context(types: Types, functions: Functions, typeBindings: TypeBindings) = Context(types, functions, typeBindings)
fun Context.bind(pattern: Pattern) = copy(typeBindings = typeBindings.push(pattern))
fun Context.plus(type: Type) = copy(types = types.plus(type))
fun Context.plus(function: Function) = copy(functions = functions.plus(function))

fun Context.unsafeCompile(script: Script): Compiled =
	compiler(this, compiled()).unsafePush(script).compiled
