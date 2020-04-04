package leo14.dispatching

import leo14.*
import leo14.Number
import leo14.untyped.*

data class Resolver(
	val typed: Typed,
	val beginFn: Leo.(Begin) -> Leo,
	val literalFn: (Literal) -> Resolver)

val emptyResolver: Resolver =
	Resolver(
		typed(emptyThunk, null),
		{ begin -> TODO() },
		{ literal -> literal.resolver })

val Literal.resolver: Resolver
	get() =
		when (this) {
			is StringLiteral -> string.resolver
			is NumberLiteral -> number.resolver
		}

val String.resolver: Resolver
	get() =
		Resolver(
			typed(thunk(value(textName)), this),
			{ begin -> TODO() },
			{ literal -> TODO() })

val Number.resolver: Resolver
	get() =
		Resolver(
			typed(thunk(value(numberName)), this),
			{ begin -> TODO() },
			{ literal -> TODO() })
