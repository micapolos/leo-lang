package leo21.token.body

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.Stack
import leo13.push
import leo13.seq
import leo13.stack
import leo21.compiled.Compiled

inline class Bindings(val bindingStack: Stack<Binding>)
val Stack<Binding>.asBindings get() = Bindings(this)
val emptyBindings = Bindings(stack())
fun Bindings.plus(binding: Binding) = bindingStack.push(binding).asBindings

fun Bindings.resolveOrNull(compiled: Compiled): Compiled? =
	bindingStack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, compiled)
	}

