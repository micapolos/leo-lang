package leo19.compiler

import leo.base.runIf
import leo13.Stack
import leo13.firstIndexed
import leo13.push
import leo13.stack
import leo14.Script
import leo19.term.invoke
import leo19.term.nullTerm
import leo19.term.term
import leo19.term.variable
import leo19.type.Arrow
import leo19.type.isStatic
import leo19.typed.Typed
import leo19.typed.nullTyped
import leo19.typed.of

data class Binding(val arrow: Arrow, val isConstant: Boolean)
data class Scope(val bindingStack: Stack<Binding>)

fun constantBinding(arrow: Arrow) = Binding(arrow, true)
fun functionBinding(arrow: Arrow) = Binding(arrow, false)

val emptyScope = Scope(stack())
fun scope(vararg bindings: Binding) = Scope(stack(*bindings))
fun Scope.plus(binding: Binding) = Scope(bindingStack.push(binding))

fun Scope.typed(script: Script): Typed =
	Compiler(this, nullTyped).plus(script).typed

fun Scope.resolveOrNull(typed: Typed): Typed? =
	bindingStack.firstIndexed { arrow.lhs == typed.type }
		?.let { indexedBinding ->
			(if (typed.type.isStatic) nullTerm else term(variable(indexedBinding.index)))
				.runIf(!indexedBinding.value.isConstant) { invoke(typed.term) }
				.of(indexedBinding.value.arrow.rhs)
		}