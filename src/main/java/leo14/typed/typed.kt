package leo14.typed

import leo.base.ifOrNull
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

val <T> Typed<T>.headOrNull: Typed<T>?
	get() =
		type.headOrNull?.let { head -> term.second of head }

val <T> Typed<T>.tailOrNull: Typed<T>?
	get() =
		type.tailOrNull?.let { tail -> term.first of tail }

fun <T> Typed<T>.resolvePlusOrNull(string: String, rhs: Typed<T>): Typed<T>? =
	when (string) {
		"head" -> ifOrNull(rhs.type == emptyType) { headOrNull }
		"tail" -> ifOrNull(rhs.type == emptyType) { tailOrNull }
		else -> null
	}

fun <T> Typed<T>.resolvePlus(string: String, rhs: Typed<T>): Typed<T> =
	resolvePlusOrNull(string, rhs) ?: plus(string, rhs)
