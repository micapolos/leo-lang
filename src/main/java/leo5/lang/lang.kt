package leo5.lang

import leo5.script.Code
import leo5.script.code
import leo5.script.span

operator fun Code.plus(code: Code) = code("plus", code)

fun float(double: Double) = float(span(double.toString()))
fun float(int: Int) = float(span(int.toString()))

fun int(int: Int) = int(span(int.toString()))

//fun bit(vararg spans: Span) = span("bit", *spans)
//fun bit(fn: CodeFn) = span("bit", fn)
//val bit get() = bit()
//
//fun center(vararg spans: Span) = span("center", *spans)
//fun center(fn: CodeFn) = span("center", fn)
//
//fun circle(vararg spans: Span) = span("circle", *spans)
//fun circle(fn: CodeFn) = span("circle", fn)
//
//val negate get() = span("negate")
//var Code.negate: Code
//	get() = code("negate")
//  set(code) { code("negate", code) }
//
//val one get() = span("one")
//
//fun plus(vararg spans: Span) = span("plus", *spans)
//fun plus(fn: CodeFn) = span("plus", fn)
//
//fun radius(vararg spans: Span) = span("radius", *spans)
//fun radius(code: Code) = span("radius", code)
//var radius: Code
//	get() = radius()
//	set(span) { radius(span) }
//
//fun x(vararg spans: Span) = span("x", *spans)
//fun x(code: Code) = span("x", code)
//var x: Code
//  get() = x()
//	set(code) {
//		x(code)
//	}
//
//fun y(vararg spans: Span) = span("y", *spans)
//fun y(code: Code) = span("y", code)
//var y: Code
//	get() = y()
//	set(code) { y(code) }
//val zero get() = span("zero")
