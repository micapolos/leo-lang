package leo21.compiled

import leo.base.notNullIf
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.lambda.value.Value
import leo21.type.Type
import leo21.typed.Typed
import leo21.typed.invoke

sealed class Binding
data class ArrowBinding(val type: Type, val typed: Typed) : Binding()
data class NameBinding(val name: String, val typed: Typed) : Binding()

fun Binding.resolveOrNull(index: Int, typed: Typed): Typed? =
	when (this) {
		is ArrowBinding -> notNullIf(typed.type == type) {
			Typed(arg(index), typed.type).invoke(typed)
		}
		is NameBinding -> null
	}

fun Binding.resolveOrNull(index: Int, name: String): Typed? =
	when (this) {
		is ArrowBinding -> null
		is NameBinding -> notNullIf(name == this.name) {
			Typed(arg(index), type)
		}
	}

fun Typed.push(binding: Binding): Typed =
	Typed(fn(valueTerm).invoke(binding.valueTerm), binding.type)

val Binding.valueTerm: Term<Value>
	get() =
		when (this) {
			is ArrowBinding -> fn(typed.valueTerm)
			is NameBinding -> typed.valueTerm
		}

val Binding.type: Type
	get() =
		when (this) {
			is ArrowBinding -> typed.type
			is NameBinding -> typed.type
		}
