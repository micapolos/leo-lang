package leo23.compiler.binding

import leo13.Stack
import leo13.linkOrNull
import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled

typealias Bindings = Stack<Binding>

fun Bindings.resolveOrNull(stackCompiled: StackCompiled, index: Int = 0): Compiled? =
	linkOrNull?.let { link ->
		link.value.resolveOrNull(index, stackCompiled)
			?: link.stack.resolveOrNull(stackCompiled, index + link.value.indexCount)
	}
