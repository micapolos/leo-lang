package leo19.compiler

import leo13.Stack
import leo13.push
import leo13.stack
import leo14.Script
import leo19.type.Arrow
import leo19.typed.Typed
import leo19.typed.nullTyped

data class Binding(val arrow: Arrow, val isConstant: Boolean)
data class Scope(val bindingStack: Stack<Binding>)

fun constantBinding(arrow: Arrow) = Binding(arrow, true)
fun functionBinding(arrow: Arrow) = Binding(arrow, false)

val emptyScope = Scope(stack())
fun scope(vararg bindings: Binding) = Scope(stack(*bindings))
fun Scope.plus(binding: Binding) = Scope(bindingStack.push(binding))

fun Scope.typed(script: Script): Typed =
	Compiler(this, nullTyped).plus(script).typed