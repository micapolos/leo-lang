package leo21.compiler

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.Stack
import leo13.push
import leo13.seq
import leo13.stack
import leo14.Script
import leo14.ScriptLine
import leo14.Scriptable
import leo14.lambda.fn
import leo14.matchInfix
import leo14.reflectOrEmptyScriptLine
import leo21.type.Type
import leo21.type.arrowTo
import leo21.type.type
import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.of
import leo21.compiled.resolvePrimOrNull

data class Bindings(val bindingStack: Stack<Binding>) : Scriptable() {
	override fun toString() = super.toString()
	override val reflectScriptLine: ScriptLine
		get() = bindingStack.reflectOrEmptyScriptLine("bindings") { reflectScriptLine }
}

val emptyBindings = Bindings(stack())
fun Bindings.push(binding: Binding) = Bindings(bindingStack.push(binding))
fun Bindings.push(type: Type) = push(givenBinding(type))

fun Bindings.resolveOrNull(compiled: Compiled): Compiled? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.applyOrNull(index, compiled)
	}

fun Bindings.resolve(compiled: Compiled): Compiled =
	null
		?: resolveOrNull(compiled)
		?: compiled.resolvePrimOrNull
		?: compiled

fun Bindings.arrowTyped(script: Script): ArrowCompiled =
	script.matchInfix("doing") { lhs, rhs ->
		lhs.type.let { lhsType ->
			push(lhsType).compiled(rhs).let { bodyTyped ->
				fn(bodyTyped.term) of (lhsType arrowTo bodyTyped.type)
			}
		}
	}!!
