package leo5.lang

import leo5.script.*
import leo5.script.code as _code
import leo5.script.span as _span

fun bit(vararg spans: Span) = _span("bit", *spans)
fun bit(fn: CodeFn) = _span("bit", fn)
val bit get() = bit()

fun center(vararg spans: Span) = _span("center", *spans)
fun center(fn: CodeFn) = _span("center", fn)

fun circle(vararg spans: Span) = _span("circle", *spans)
fun circle(fn: CodeFn) = _span("circle", fn)

fun float(vararg spans: Span) = _span("float", *spans)
fun float(double: Double) = float(_span(double.toString()))
fun float(int: Int) = float(_span(int.toString()))

fun int(vararg spans: Span) = _span("int", *spans)
fun int(int: Int) = int(_span(int.toString()))

val negate get() = _span("negate")
val Code.negate get() = _code("negate")

val one get() = _span("one")

fun plus(vararg spans: Span) = _span("plus", *spans)
fun plus(fn: CodeFn) = _span("plus", fn)
operator fun Code.plus(code: Code) = _code("plus", code)

fun radius(vararg spans: Span) = _span("radius", *spans)
fun radius(code: Code) = _span("radius", code)
var radius: Code
	get() = radius()
	set(span) { radius(span) }

fun x(vararg spans: Span) = _span("x", *spans)
fun x(code: Code) = _span("x", code)
var x: Code
  get() = x()
	set(code) {
		x(code)
	}

fun y(vararg spans: Span) = _span("y", *spans)
fun y(code: Code) = _span("y", code)
var y: Code
	get() = y()
	set(code) {
		y(code)
	}

val zero get() = _span("zero")
