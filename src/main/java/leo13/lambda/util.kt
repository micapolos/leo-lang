package leo13.lambda

import leo13.Index
import leo13.index

fun <T> fn(body: Value<T>) = value(abstraction(body))
fun <T> fn2(body: Value<T>) = fn(fn(body))
fun <T> fn3(body: Value<T>) = fn(fn2(body))

operator fun <T> Value<T>.invoke(value: Value<T>) = value(application(this, value))

fun <T> arg(index: Index): Value<T> = value(variable(index))
fun <T> arg0(): Value<T> = arg(index(0))
fun <T> arg1(): Value<T> = arg(index(1))
fun <T> arg2(): Value<T> = arg(index(2))

fun <T> first(): Value<T> = fn2(arg1())
fun <T> second(): Value<T> = fn2(arg0())
fun <T> Value<T>.switch(firstFn: Value<T>, secondFn: Value<T>) = this(firstFn)(secondFn)

fun <T> pair(lhs: Value<T>, rhs: Value<T>) = fn3(arg0<T>()(arg2())(arg1()))(lhs)(rhs)

val <T> Value<T>.first get() = this(first())
val <T> Value<T>.second get() = this(second())

fun <T> list() = pair<T>(first(), first())
fun <T> Value<T>.append(head: Value<T>) = pair(second(), pair(this, head))
val <T> Value<T>.isEmpty get() = first