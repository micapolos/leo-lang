package leo14.untyped.dsl

import leo14.literal
import leo14.untyped.Value
import leo14.untyped.value
import leo14.untyped.valueTo

// Core
typealias V = Value

fun v(name: String, vararg v: V): Value = name valueTo _program(*v)

// Primitives
fun number(int: Int): Value = value(literal(int))
fun text(string: String): Value = value(literal(string))
fun _program(vararg v: V) = leo14.untyped.program(*v)

// DSL
fun _as(vararg v: V) = v("as", *v)
fun assert(vararg v: V) = v("assert", *v)
fun bar(vararg v: V) = v("bar", *v)
fun check(vararg v: V) = v("check", *v)
fun decrement(vararg v: V) = v("decrement", *v)
fun equals(vararg v: V) = v("equals", *v)
fun expect(vararg v: V) = v("expect", *v)
fun factorial(vararg v: V) = v("factorial", *v)
fun foo(vararg v: V) = v("foo", *v)
fun gave(vararg v: V) = v("gave", *v)
fun give(vararg v: V) = v("give", *v)
fun given(vararg v: V) = v("given", *v)
fun gives(vararg v: V) = v("gives", *v)
fun increment(vararg v: V) = v("increment", *v)
fun _is(vararg v: V) = v("is", *v)
fun minus(vararg v: V) = v("minus", *v)
fun number(vararg v: V) = v("number", *v)
fun one(vararg v: V) = v("one", *v)
fun plus(vararg v: V) = v("plus", *v)
fun point(vararg v: V) = v("point", *v)
fun quote(vararg v: V) = v("quote", *v)
fun recursive(vararg v: V) = v("recursive", *v)
fun save(vararg v: V) = v("save", *v)
fun should(vararg v: V) = v("should", *v)
fun test(vararg v: V) = v("test", *v)
fun text(vararg v: V) = v("text", *v)
fun times(vararg v: V) = v("times", *v)
fun unquote(vararg v: V) = v("unquote", *v)
fun vector(vararg v: V) = v("vector", *v)
fun x(vararg v: V) = v("x", *v)
fun y(vararg v: V) = v("y", *v)
fun z(vararg v: V) = v("z", *v)
fun zero(vararg v: V) = v("zero", *v)
