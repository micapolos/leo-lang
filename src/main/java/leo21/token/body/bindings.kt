package leo21.token.body

import leo.base.indexed
import leo.base.mapFirstOrNull
import leo13.Stack
import leo13.push
import leo13.seq
import leo13.stack
import leo21.compiled.Compiled

data class Bindings(val stack: Stack<Binding>)

val Stack<Binding>.asBindings get() = Bindings(this)
val emptyBindings = Bindings(stack())
fun Bindings.plus(binding: Binding): Bindings = stack.push(binding).asBindings

fun Bindings.resolveOrNull(compiled: Compiled): Compiled? =
	stack.seq.indexed.mapFirstOrNull {
		value.resolveOrNull(index, compiled)
	}
