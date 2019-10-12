@file:Suppress("UNCHECKED_CAST")

package leo13

object Eval

val eval = Eval

fun fn(fn: (Any?) -> Any?) = fn
fun lazy(fn: (Eval) -> Any?) = fn
fun intFn(fn: (Int) -> Any?) = fn
fun listFn(fn: (List<Any?>) -> Any?) = fn

infix fun Any?.eat(any: Any?) = (this as (Any?.() -> Any?)).invoke(any)
infix fun Any?.dot(any: Any?) = fn { eat(any.eat(it)) }

val intInc = intFn { it.inc() }
val intAdd = intFn { a -> intFn { b -> a + b } }
val listAt = listFn { list -> intFn { index -> list[index] } }
