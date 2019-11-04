package leo14.typed

import leo14.lambda.*

data class Typed<out T>(val term: Term<T>, val type: Type)

infix fun <T> Term<T>.of(type: Type) = Typed(this, type)

fun <T> Typed<T>.plus(string: String, rhs: Typed<T>): Typed<T> =
	term.typedPlus(rhs.term) of type.plus(string fieldTo rhs.type)

fun <T> emptyTyped() = id<T>() of emptyType

val <T> Typed<T>.isEmpty get() = this == emptyTyped<T>()

fun <T> Term<T>.typedPlus(rhs: Term<T>) =
	if (this == id<T>()) rhs
	else pair(this, rhs)

val <T> Term<T>.typedHead get() = second
val <T> Term<T>.typedTail get() = first

val <T> Typed<T>.headOrNull: Typed<T>?
	get() =
		type.headOrNull?.let { head -> term.typedHead of head }

val <T> Typed<T>.tailOrNull: Typed<T>?
	get() =
		type.tailOrNull?.let { tail -> term.typedTail of tail }

fun <T> Typed<T>.resolve(string: String, rhs: Typed<T>): Typed<T>? =
	if (rhs.isEmpty) resolve(string)
	else null

fun <T> Typed<T>.resolve(string: String): Typed<T>? =
	when (string) {
		"head" -> headOrNull
		"tail" -> tailOrNull
		else -> resolveAccess(string)
	}

fun <T> Typed<T>.resolveAccess(string: String): Typed<T>? =
	when (type) {
		is LinkType -> (term of type.link.field.type).resolveGet(string) ?: wrap(string)
		else -> null
	}

fun <T> Typed<T>.resolveGet(string: String): Typed<T>? =
	when (type) {
		is LinkType ->
			if (type.link.field.string == string) term.typedHead of type(string fieldTo type.link.field.type)
			else (term.typedTail of type.link.lhs).resolveGet(string)
		else -> null
	}

fun <T> Typed<T>.eval(string: String, rhs: Typed<T>): Typed<T> =
	resolve(string, rhs) ?: plus(string, rhs)

fun <T> Typed<T>.wrap(string: String) =
	term of type(string fieldTo type)