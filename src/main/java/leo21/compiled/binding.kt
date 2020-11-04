package leo21.compiled

import leo.base.notNullIf
import leo14.lambda.arg
import leo14.lambda.invoke
import leo21.prim.Prim
import leo21.type.Arrow
import leo21.type.Type
import leo21.type.make
import leo21.type.onlyNameOrNull
import leo21.typed.Typed
import leo21.typed.getOrNull
import leo21.typed.make

sealed class Binding
data class ConstantBinding(val arrow: Arrow) : Binding()
data class FunctionBinding(val arrow: Arrow) : Binding()
data class GivenBinding(val type: Type) : Binding()

fun constantBinding(arrow: Arrow): Binding = ConstantBinding(arrow)
fun functionBinding(arrow: Arrow): Binding = FunctionBinding(arrow)
fun givenBinding(type: Type): Binding = GivenBinding(type)

fun Binding.resolveOrNull(index: Int, typed: Typed): Typed? =
	when (this) {
		is ConstantBinding -> notNullIf(typed.type == arrow.lhs) {
			Typed(arg(index), arrow.rhs)
		}
		is FunctionBinding -> notNullIf(typed.type == arrow.lhs) {
			Typed(arg<Prim>(index).invoke(typed.term), arrow.rhs)
		}
		is GivenBinding -> typed.type.onlyNameOrNull?.let { name ->
			when (name) {
				"given" -> Typed(arg(index), type).make("given")
				else -> Typed(arg(index), type).make("given").getOrNull(name)
			}
		}
	}

fun Binding.resolveOrNull(index: Int, name: String): Typed? =
	when (this) {
		is ConstantBinding -> null
		is FunctionBinding -> null
		is GivenBinding ->
			if (name == "given") Typed(arg(index), type).make("given").getOrNull(name)
			else Typed(arg(index), type).make("given").getOrNull(name)
	}
