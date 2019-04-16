package leo32.runtime

import leo.base.empty

data class TermBuilder(
	var term: Term)

val Term.builder
	get() =
		TermBuilder(this)

fun TermBuilder.plus(string: String, fn: TermBuilder.() -> Unit): TermBuilder {
	term = term.plus(string) {
		val termBuilder = builder
		termBuilder.fn()
		termBuilder.term
	}
	return this
}

fun TermBuilder.plus(string: String): TermBuilder =
	plus(string) { this }

fun _term(fn: TermBuilder.() -> Unit): Term {
	val termBuilder = empty.term.builder
	termBuilder.fn()
	return termBuilder.term.descope
}

// dsl

typealias T = TermBuilder
typealias Fn = TermBuilder.() -> Unit

fun T.actual(fn: Fn) = plus("actual", fn)
fun T.argument(fn: Fn) = plus("argument", fn)
fun T.bit(fn: Fn) = plus("bit", fn)
fun T.case(fn: Fn) = plus("case", fn)
fun T.center(fn: Fn) = plus("center", fn)
fun T.circle(fn: Fn) = plus("circle", fn)
fun T.define(fn: Fn) = plus("define", fn)
fun T.either(fn: Fn) = plus("either", fn)
fun T.error(fn: Fn) = plus("error", fn)
fun T.expected(fn: Fn) = plus("expected", fn)
fun T.gives(fn: Fn) = plus("gives", fn)
fun T.has(fn: Fn) = plus("has", fn)
fun T.not(fn: Fn) = plus("not", fn)
fun T.one(fn: Fn) = plus("one", fn)
fun T.quote(fn: Fn) = plus("quote", fn)
fun T.radius(fn: Fn) = plus("radius", fn)
fun T.switch(fn: Fn) = plus("switch", fn)
fun T.test(fn: Fn) = plus("test", fn)
fun T.to(fn: Fn) = plus("to", fn)
fun T.x(fn: Fn) = plus("x", fn)
fun T.y(fn: Fn) = plus("y", fn)
fun T.zero(fn: Fn) = plus("zero", fn)

val T.argument get() = argument { Unit }
val T.bit get() = bit { Unit }
val T.center get() = center { Unit }
val T.circle get() = circle { Unit }
val T.not get() = not { Unit }
val T.radius get() = radius { Unit }
val T.one get() = one { Unit }
val T.x get() = x { Unit }
val T.y get() = y { Unit }
val T.zero get() = zero { Unit }

fun T.int(int: Int): T = plus("int") { plus(int.toString()) }
