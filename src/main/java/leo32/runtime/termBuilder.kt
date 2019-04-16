package leo32.runtime

import leo.base.empty

data class TermBuilder(
	var term: Term)

val Term.builder
	get() =
		TermBuilder(this)

fun TermBuilder.plus(string: String, fn: TermBuilder.() -> Unit): TermBuilder {
	val termBuilder = term.begin.builder
	termBuilder.fn()
	term = term.plusResolved(string to termBuilder.term)
	return this
}

fun TermBuilder.plus(string: String): TermBuilder =
	plus(string) { this }

fun term(fn: TermBuilder.() -> Unit): Term {
	val termBuilder = empty.term.builder
	termBuilder.fn()
	return termBuilder.term.descope
}

// dsl

typealias T = TermBuilder
typealias Fn = TermBuilder.() -> Unit

fun T.bit(fn: Fn) = plus("bit", fn)
fun T.case(fn: Fn) = plus("case", fn)
fun T.center(fn: Fn) = plus("center", fn)
fun T.circle(fn: Fn) = plus("circle", fn)
fun T.either(fn: Fn) = plus("either", fn)
fun T.gives(fn: Fn) = plus("gives", fn)
fun T.has(fn: Fn) = plus("has", fn)
fun T.one(fn: Fn) = plus("one", fn)
fun T.radius(fn: Fn) = plus("radius", fn)
fun T.switch(fn: Fn) = plus("switch", fn)
fun T.to(fn: Fn) = plus("to", fn)
fun T.x(fn: Fn) = plus("x", fn)
fun T.y(fn: Fn) = plus("y", fn)
fun T.zero(fn: Fn) = plus("zero", fn)

val T.bit get() = bit { Unit }
val T.center get() = center { Unit }
val T.circle get() = circle { Unit }
val T.radius get() = radius { Unit }
val T.one get() = one { Unit }
val T.x get() = x { Unit }
val T.y get() = y { Unit }
val T.zero get() = zero { Unit }

fun T.int(int: Int): T = plus("int") { plus(int.toString()) }
