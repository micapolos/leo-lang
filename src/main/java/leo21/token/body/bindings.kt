package leo21.token.body

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.Stack
import leo13.push
import leo13.seq
import leo13.stack
import leo14.ScriptLine
import leo14.Scriptable
import leo14.anyReflectScriptLine
import leo14.lambda.value.Scope
import leo14.lambda.value.Value
import leo14.lambda.value.at
import leo14.reflectOrEmptyScriptLine
import leo21.compiled.Compiled
import leo21.compiled.resolve
import leo21.evaluator.Evaluated
import leo21.prim.Prim
import java.lang.reflect.Type

data class Bindings(val bindingStack: Stack<Binding>) : Scriptable() {
	override val reflectScriptLine: ScriptLine
		get() = bindingStack.reflectOrEmptyScriptLine("bindings") { reflectScriptLine }
}

val Stack<Binding>.asBindings get() = Bindings(this)
val emptyBindings = Bindings(stack())
fun Bindings.plus(binding: Binding) = bindingStack.push(binding).asBindings

fun Bindings.resolveOrNull(compiled: Compiled): Compiled? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, compiled)
	}

fun Bindings.resolveOrNull(scope: Scope<Prim>, evaluated: Evaluated): Evaluated? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(scope.at(index), evaluated)
	}

fun Bindings.resolve(compiled: Compiled): Compiled =
	resolveOrNull(compiled) ?: compiled.resolve
