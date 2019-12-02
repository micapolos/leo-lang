package leo14.typed.compiler

import leo13.Index
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.typed.Type
import leo14.typed.Typed
import leo14.typed.of

sealed class MemoryValue<T>

data class ArgumentMemoryValue<T>(val type: Type) : MemoryValue<T>()
data class BindingMemoryValue<T>(val binding: MemoryBinding<T>) : MemoryValue<T>()

fun <T> argumentMemoryValue(type: Type): MemoryValue<T> = ArgumentMemoryValue(type)
fun <T> value(binding: MemoryBinding<T>): MemoryValue<T> = BindingMemoryValue(binding)

fun <T> Term<T>.resolveForEnd(value: MemoryValue<T>): Term<T> =
	when (value) {
		is ArgumentMemoryValue -> fn(this)
		is BindingMemoryValue -> fn(this).invoke(value.binding.typed.term)
	}

fun <T> MemoryValue<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	arg<T>(index)
		.let { indexTerm ->
			when (this) {
				is ArgumentMemoryValue -> indexTerm of type
				is BindingMemoryValue ->
					if (binding.isAction) indexTerm.invoke(term) of binding.typed.type
					else indexTerm of binding.typed.type
			}
		}

val <T> MemoryValue<T>.type
	get() =
		when (this) {
			is ArgumentMemoryValue -> type
			is BindingMemoryValue -> binding.typed.type
		}
