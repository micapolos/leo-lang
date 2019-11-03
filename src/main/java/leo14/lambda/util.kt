package leo14.lambda

import leo13.Index
import leo13.index

fun <T> fn(body: Term<T>) = term(abstraction(body))
fun <T> fn2(body: Term<T>) = fn(fn(body))
fun <T> fn3(body: Term<T>) = fn(fn2(body))

operator fun <T> Term<T>.invoke(term: Term<T>) = term(application(this, term))

fun <T> arg(index: Index): Term<T> = term(variable(index))
fun <T> arg0(): Term<T> = arg(index(0))
fun <T> arg1(): Term<T> = arg(index(1))
fun <T> arg2(): Term<T> = arg(index(2))

fun <T> first(): Term<T> = fn2(arg1())
fun <T> second(): Term<T> = fn2(arg0())
fun <T> Term<T>.switch(firstFn: Term<T>, secondFn: Term<T>) = this(firstFn)(secondFn)

fun <T> pair(lhs: Term<T>, rhs: Term<T>) = fn3(arg0<T>()(arg2())(arg1()))(lhs)(rhs)

val <T> Term<T>.first get() = this(first())
val <T> Term<T>.second get() = this(second())

fun <T> list() = pair<T>(first(), first())
fun <T> Term<T>.append(head: Term<T>) = pair(second(), pair(this, head))
val <T> Term<T>.isEmpty get() = first

// === boolean

fun <T> term(boolean: Boolean): Term<T> =
	if (boolean) second()
	else first()

fun <T> Term<T>.booleanOrNull() =
	when (this) {
		first<T>() -> false
		second<T>() -> true
		else -> null
	}