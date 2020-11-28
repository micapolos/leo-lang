package leo23.compiler

import leo13.Stack
import leo13.stack
import leo23.compiler.binding.Binding
import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled
import leo23.typed.term.resolve
import leo23.typed.term.stackCompiled

data class Context(val bindingStack: Stack<Binding>)

val emptyContext get() = Context(stack())

val Context.begin: Context get() = this

fun Context.resolve(stackCompiled: StackCompiled): StackCompiled =
	resolveOrNull(stackCompiled)?.stackCompiled ?: stackCompiled.resolve

fun Context.resolveOrNull(stackCompiled: StackCompiled): Compiled? =
	null
