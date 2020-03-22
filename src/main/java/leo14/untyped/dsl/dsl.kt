package leo14.untyped.dsl

import leo14.literal
import leo14.untyped.Value
import leo14.untyped.value
import leo14.untyped.valueTo

// Core
typealias V = Value

fun v(name: String, vararg v: V) = name valueTo _program(*v)

// Primitives
fun value(int: Int) = value(literal(int))
fun value(string: String) = value(literal(string))
fun _program(vararg v: V) = leo14.untyped.program(*v)

// DSL
fun decrement(vararg v: V) = v("decrement", *v)
fun factorial(vararg v: V) = v("factorial", *v)
fun foo(vararg v: V) = v("foo", *v)
fun given(vararg v: V) = v("given", *v)
fun gives(vararg v: V) = v("gives", *v)
fun minus(vararg v: V) = v("minus", *v)
fun number(vararg v: V) = v("number", *v)
fun plus(vararg v: V) = v("plus", *v)
fun quote(vararg v: V) = v("quote", *v)
fun recursive(vararg v: V) = v("recursive", *v)
fun times(vararg v: V) = v("times", *v)
fun unquote(vararg v: V) = v("unquote", *v)
