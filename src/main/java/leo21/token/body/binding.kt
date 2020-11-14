package leo21.token.body

import leo21.compiled.ArrowCompiled
import leo21.compiled.Compiled
import leo21.compiled.resolveOrNull

sealed class Binding
data class CompiledBinding(val compiled: Compiled) : Binding()
data class ArrowCompiledBinding(val arrowCompiled: ArrowCompiled) : Binding()
data class GivenBinding(val given: Given) : Binding()

val Compiled.asBinding: Binding get() = CompiledBinding(this)
val Given.asBinding: Binding get() = GivenBinding(this)

fun Binding.resolveOrNull(index: Int, param: Compiled): Compiled? =
	when (this) {
		is CompiledBinding -> null
		is ArrowCompiledBinding -> arrowCompiled.resolveOrNull(index, param)
		is GivenBinding -> given.resolveOrNull(index, param)
	}