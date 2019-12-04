package leo14.typed

import leo.base.notNullIf
import leo14.lambda.invoke

data class Function<T>(val takes: Type, val does: Typed<T>)

infix fun <T> Type.does(body: Typed<T>) = Function(this, body)

fun <T> Function<T>.applyOrNull(typed: Typed<T>): Typed<T>? =
	notNullIf(takes == typed.type) {
		does.term.invoke(typed.term) of does.type
	}
