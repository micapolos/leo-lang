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
import leo14.plus
import leo14.reflectOrEmptyScript
import leo14.script
import leo19.decompiler.script
import leo19.term.boolean
import leo19.typed.Typed
import leo19.typed.eval
import leo19.typed.typed
import leo19.typed.typedEquals

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
	emptyContext.compiler(typed()).plus(script).compiledTyped

fun Resolver.testEquals(lhs: Script, rhs: Script) {
	typed(lhs).eval.let { lhsTyped ->
		typed(rhs).eval.let { rhsTyped ->
			lhsTyped.typedEquals(rhsTyped)
				.eval
				.term
				.boolean
				.let { passed ->
					if (!passed) error(
						script(
							"error" lineTo script(
								"test" lineTo lhs.plus("equals" lineTo rhs),
								"gave" lineTo lhsTyped.script.plus("equals" lineTo rhsTyped.script)))
							.toString())
				}
		}
	}
}