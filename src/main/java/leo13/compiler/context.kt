package leo13.compiler

import leo13.script.Script
import leo13.script.Scriptable
import leo13.script.script
import leo13.type.Type
import leo13.type.TypeBindings
import leo13.type.push
import leo13.type.typeBindings

data class Context(
	val traces: Traces,
	val functions: Functions,
	val typeBindings: TypeBindings) : Scriptable() {
	override fun toString() = super.toString()
	override val scriptableName get() = "context"
	override val scriptableBody
		get() = script(
			traces.scriptableLine,
			functions.scriptableLine,
			typeBindings.scriptableLine)
}

fun context() = Context(traces(), functions(), typeBindings())
fun context(traces: Traces, functions: Functions, typeBindings: TypeBindings) = Context(traces, functions, typeBindings)
fun Context.bind(type: Type) = copy(typeBindings = typeBindings.push(type))
fun Context.plus(trace: Trace) = copy(traces = traces.plus(trace))
fun Context.plus(function: Function) = copy(functions = functions.plus(function))

fun Context.unsafeCompile(script: Script): Compiled =
	compiler(this, compiled()).unsafePush(script).compiled
