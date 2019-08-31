package leo13.compiler

import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.Switch
import leo13.script.script
import leo13.type.Type
import leo13.type.TypeBindings
import leo13.type.push
import leo13.type.typeBindings

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
fun Context.bind(type: Type) = copy(typeBindings = typeBindings.push(type))
fun Context.plus(type: Type) = copy(types = types.plus(type))
fun Context.plus(function: Function) = copy(functions = functions.plus(function))

fun Context.unsafeCompile(script: Script): Compiled =
	compiler(this, compiled()).unsafePush(script).compiled

fun Context.compile(switch: Switch): SwitchCompiled =
	compiler(this, null).push(switch).compiled