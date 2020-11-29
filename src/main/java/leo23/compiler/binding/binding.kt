package leo23.compiler.binding

import leo23.typed.term.Compiled
import leo23.typed.term.StackCompiled

sealed class Binding
data class FunctionBinding(val function: BindingFunction) : Binding()
data class ConstantBinding(val constant: BindingConstant) : Binding()
data class GivenBinding(val given: Given) : Binding()

fun Binding.resolveOrNull(index: Int, stackCompiled: StackCompiled): Compiled? =
	when (this) {
		is FunctionBinding -> function.resolveOrNull(index, stackCompiled)
		is ConstantBinding -> constant.resolveOrNull(index, stackCompiled)
		is GivenBinding -> given.resolveOrNull(index, stackCompiled)
	}

val Binding.indexCount: Int
	get() =
		when (this) {
			is FunctionBinding -> function.indexCount
			is ConstantBinding -> constant.indexCount
			is GivenBinding -> given.indexCount
		}
