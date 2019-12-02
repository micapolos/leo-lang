package leo14.typed

import leo.base.notNullIf
import leo.base.notNullOrError
import leo14.lambda.invoke

data class Function<T>(val takes: Type, val does: Typed<T>)

infix fun <T> Type.does(body: Typed<T>) = Function(this, body)

fun <T> Function<T>.resolve(typed: Typed<T>): Typed<T>? =
	notNullIf(takes == typed.type) {
		does.term.invoke(typed.term) of does.type
	}

fun <T> Function<T>.apply(typed: Typed<T>): Typed<T> =
	resolve(typed).notNullOrError("$this.give($typed)")