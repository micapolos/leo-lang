package leo14.typed.compiler

import leo13.Index
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.fn
import leo14.lambda.invoke
import leo14.typed.Function
import leo14.typed.Type
import leo14.typed.Typed
import leo14.typed.compiler.Definition.Kind
import leo14.typed.compiler.Definition.Kind.ACTION
import leo14.typed.compiler.Definition.Kind.VALUE
import leo14.typed.of

data class Definition<T>(val kind: Kind, val function: Function<T>) {
	enum class Kind { VALUE, ACTION }
}

fun <T> definition(kind: Kind, function: Function<T>): Definition<T> =
	Definition(kind, function)

fun <T> Definition<T>.matches(type: Type): Boolean =
	function.takes == type

fun <T> Definition<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	arg<T>(index)
		.run {
			when (kind) {
				ACTION -> invoke(term)
				VALUE -> this
			}
		}
		.of(function.does.type)

fun <T> Typed<T>.ret(definition: Definition<T>): Typed<T> =
	when (definition.kind) {
		ACTION -> fn(term) of type
		VALUE -> this
	}

