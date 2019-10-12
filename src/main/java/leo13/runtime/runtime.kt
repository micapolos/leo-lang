@file:Suppress("UNCHECKED_CAST")

package leo13.runtime

typealias X = Any?
typealias Fn = (X) -> X

object Eval
val eval = Eval

fun fn(fn: Fn): X = fn
fun lazy(fn: () -> X) = fn { (it as Eval).run { fn() } }
fun intFn(fn: (Int) -> X) = fn { fn(it as Int) }
fun indexedFn(fn: (IndexedValue<X>) -> X) = fn { fn(it as IndexedValue<X>) }
fun arrayFn(fn: (Array<X>) -> X) = fn { fn(it as Array<X>) }

infix fun Any?.eat(any: X) = (this as Fn).invoke(any)
fun Any?.eatArray(vararg any: X) = eat(any)
infix fun Any?.dot(any: X) = fn { eat(any.eat(it)) }

val intInc = intFn { it.inc() }
val intAdd = intFn { a -> intFn { b -> a + b } }
val arrayAt = arrayFn { array -> intFn { index -> array[index] } }

val switch = arrayFn { cases ->
	indexedFn { indexed ->
		cases[indexed.index] eat indexed.value
	}
}
