package leo19.compiler

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.Stack
import leo13.push
import leo13.seq
import leo13.stack
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.reflectOrEmptyScript
import leo19.typed.Typed

data class Resolver(val bindingStack: Stack<Binding>) {
	override fun toString() = reflect.toString()
}

val Resolver.reflect: ScriptLine
	get() =
		"resolver" lineTo bindingStack.reflectOrEmptyScript { reflect }

val emptyResolver = Resolver(stack())
fun resolver(vararg bindings: Binding) = Resolver(stack(*bindings))
fun Resolver.plus(binding: Binding) = Resolver(bindingStack.push(binding))

fun Resolver.resolveOrNull(typed: Typed): Typed? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.let { binding ->
			binding.resolveOrNull(typed, index)
		}
	}

fun Resolver.resolve(typed: Typed): Typed =
	resolveOrNull(typed) ?: typed

fun Resolver.typed(script: Script): Typed =
	emptyContext.typed(script)
