package leo13.lambda

fun <T> fn(body: Value<T>) = value(abstraction(body))
fun <T> fn2(body: Value<T>) = fn(fn(body))
fun <T> fn3(body: Value<T>) = fn(fn2(body))

operator fun <T> Value<T>.invoke(value: Value<T>) = value(application(this, value))

fun <T> arg(index: Int): Value<T> = value(variable(index))
fun <T> arg0(): Value<T> = arg(0)
fun <T> arg1(): Value<T> = arg(1)
fun <T> arg2(): Value<T> = arg(2)

fun <T> first(): Value<T> = fn2(arg(1))
fun <T> second(): Value<T> = fn2(arg(0))

fun <T> pair(lhs: Value<T>, rhs: Value<T>) = fn3(arg<T>(0)(arg(2))(arg(1)))(lhs)(rhs)

val <T> Value<T>.first get() = this(first())
val <T> Value<T>.second get() = this(second())
