package leo21.compiled

import leo.base.notNullIf
import leo14.lambda.arg
import leo14.lambda.invoke
import leo21.prim.Prim
import leo21.type.Arrow
import leo21.type.Type
import leo21.typed.Typed
import leo21.typed.getOrNull
import leo21.typed.make

sealed class Binding
data class ArrowBinding(val arrow: Arrow) : Binding()
data class TypeBinding(val type: Type) : Binding()

fun binding(arrow: Arrow): Binding = ArrowBinding(arrow)
fun binding(type: Type): Binding = TypeBinding(type)

fun Binding.resolveOrNull(index: Int, typed: Typed): Typed? =
	when (this) {
		is ArrowBinding -> notNullIf(typed.type == arrow.lhs) {
			Typed(arg<Prim>(index).invoke(typed.term), arrow.rhs)
		}
		is TypeBinding -> null
	}

fun Binding.resolveOrNull(index: Int, name: String): Typed? =
	when (this) {
		is ArrowBinding -> null
		is TypeBinding -> Typed(arg(index), type).make("given").getOrNull(name)
	}
