package leo13

fun fn(fn: (Any?) -> Any?) = fn

infix fun Any?.eat(any: Any?) = (this as (Any?.() -> Any?)).invoke(any)
infix fun Any?.dot(any: Any?) = fn { a -> eat(any.eat(a)) }

val intInc = fn { a -> (a as Int).inc() }
val intAdd = fn { a -> fn { b -> a as Int + b as Int } }
val listAt = fn { list -> fn { index -> (list as List<Any?>)[index as Int] } }

