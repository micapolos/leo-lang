package leo32.leo

import leo.base.empty
import leo32.runtime.Term
import leo32.runtime.descope
import leo32.runtime.plus
import leo32.runtime.term

data class Context(
	var term: Term)

typealias T = Context
typealias Leo = Context.() -> Unit

val Term.builder
	get() =
		Context(this)

fun Context.plus(string: String, fn: Context.() -> Unit): Context {
	term = term.plus(string) {
		val termBuilder = builder
		termBuilder.fn()
		termBuilder.term
	}
	return this
}

fun Context.plus(string: String): Context =
	plus(string) { this }

fun _term(leo: Leo): Term {
	val termBuilder = empty.term.builder
	termBuilder.leo()
	return termBuilder.term.descope
}

fun _leo(leo: Leo) {
	val termBuilder = empty.term.builder
	termBuilder.leo()
}

// dsl

fun T.int(int: Int): T = plus("int") { plus(int.toString()) }

fun T._import(leo: Leo) = leo()

val circle: Leo = {
	circle {
		center {
			x { int(12) }
			y { int(13) }
		}
		radius { int(14) }
	}
}

val radius: Leo = {
	_import { circle }
	circle.radius.x
}

