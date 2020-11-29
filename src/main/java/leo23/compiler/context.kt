package leo23.compiler

import leo13.push
import leo13.stack
import leo23.compiler.binding.Bindings
import leo23.compiler.binding.Given
import leo23.compiler.binding.GivenBinding
import leo23.compiler.binding.resolveOrNull
import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled
import leo23.typed.term.resolve
import leo23.typed.term.stackCompiled

data class Context(val bindings: Bindings)

val emptyContext get() = Context(stack())

val Context.begin: Context get() = this

fun Context.beginDo(stackCompiled: StackCompiled): Context =
	copy(bindings = bindings.push(GivenBinding(Given(stackCompiled.t))))

fun Context.resolve(stackCompiled: StackCompiled): StackCompiled =
	resolveOrNull(stackCompiled)?.stackCompiled ?: stackCompiled.resolve

fun Context.resolveOrNull(stackCompiled: StackCompiled): Compiled? =
	bindings.resolveOrNull(stackCompiled)
