package leo21.compiled

import leo.base.notNullIf
import leo14.lambda.arg
import leo14.lambda.invoke
import leo21.value.Value
import leo21.type.Arrow
import leo21.type.Type
import leo21.typed.Typed
import leo21.typed.getOrNull
import leo21.typed.make

sealed class Binding
data class ArrowBinding(val arrow: Arrow) : Binding()
data class TypeBinding(val type: Type) : Binding()

fun Binding.resolveOrNull(index: Int, typed: Typed): Typed? =
	when (this) {
		is ArrowBinding -> notNullIf(typed.type == arrow.lhs) {
			Typed(arg<Value>(index).invoke(typed.valueTerm), arrow.rhs)
		}
		is TypeBinding -> null
	}

fun Binding.resolveOrNull(index: Int, name: String): Typed? =
	when (this) {
		is ArrowBinding -> null
		is TypeBinding -> Typed(arg(index), type).make("given").getOrNull(name)
	}

//fun Typed.push(binding: Binding): Typed =
//	Typed(fn(valueTerm).invoke(binding.valueTerm), binding.type)
//
//val Binding.valueTerm: Term<Value>
//	get() =
//		when (this) {
//			is ArrowBinding -> fn(typed.valueTerm)
//			is GivenBinding -> typed.valueTerm
//		}
//
//val Binding.type: Type
//	get() =
//		when (this) {
//			is ArrowBinding -> typed.type
//			is GivenBinding -> typed.type
//		}
