package leo14.typed

import leo.base.notNullIf
import leo.base.notNullOrError
import leo14.lambda.invoke

data class Action<T>(val param: Type, val body: Typed<T>)

infix fun <T> Type.does(body: Typed<T>) = Action(this, body)

fun <T> Action<T>.resolve(typed: Typed<T>): Typed<T>? =
	notNullIf(param == typed.type) {
		body.term.invoke(typed.term) of body.type
	}

fun <T> Action<T>.`do`(typed: Typed<T>): Typed<T> =
	resolve(typed).notNullOrError("$this.do($typed)")