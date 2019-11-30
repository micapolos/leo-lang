package leo14.typed.compiler

import leo13.Index
import leo14.lambda.Term
import leo14.lambda.arg
import leo14.lambda.invoke
import leo14.typed.Function
import leo14.typed.Type
import leo14.typed.Typed
import leo14.typed.compiler.Definition.Kind
import leo14.typed.compiler.Definition.Kind.ACTION
import leo14.typed.compiler.Definition.Kind.VALUE
import leo14.typed.of

data class Definition<T>(val function: Function<T>, val kind: Kind) {
	enum class Kind { VALUE, ACTION }
}

fun <T> definition(function: Function<T>, kind: Kind): Definition<T> =
	Definition(function, kind)

fun <T> Definition<T>.matches(type: Type): Boolean =
	function.takes == type

fun <T> Definition<T>.resolve(index: Index, term: Term<T>): Typed<T>? =
	arg<T>(index)
		.run {
			when (kind) {
				ACTION -> invoke(term)
				VALUE -> term
			}
		}
		.of(function.does.type)

