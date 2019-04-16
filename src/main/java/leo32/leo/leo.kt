package leo32.leo

import leo.base.empty
import leo32.runtime.Term
import leo32.runtime.descope
import leo32.runtime.plus
import leo32.runtime.term

data class Builder(var term: Term)

typealias T = Builder
typealias Leo = Builder.() -> Unit

val Term.builder
	get() =
		Builder(this)

fun Builder.plus(string: String, fn: Builder.() -> Unit): Builder {
	term = term.plus(string) {
		val termBuilder = builder
		termBuilder.fn()
		termBuilder.term
	}
	return this
}

fun Builder.plus(string: String): Builder =
	plus(string) { Unit }

fun _term(leo: Leo): Term {
	val termBuilder = empty.term.builder
	termBuilder.leo()
	return termBuilder.term.descope
}

fun T.int(int: Int): T = plus("int") { plus(int.toString()) }

fun T._import(leo: Leo) = leo()
