@file:Suppress("UNCHECKED_CAST")

package leo13

typealias X = Any?
typealias Fn = (X) -> X

object Eval
val eval = Eval

fun fn(fn: Fn): X = fn
fun lazy(fn: () -> X) = fn { (it as Eval).run { fn() } }
fun intFn(fn: (Int) -> X) = fn { fn(it as Int) }
fun indexedFn(fn: (IndexedValue<X>) -> X) = fn { fn(it as IndexedValue<X>) }
fun listFn(fn: (List<X>) -> X) = fn { fn(it as List<X>) }

infix fun Any?.eat(any: X) = (this as Fn).invoke(any)
fun Any?.eatList(vararg any: X) = eat(any.toList())
infix fun Any?.dot(any: X) = fn { eat(any.eat(it)) }

val intInc = intFn { it.inc() }
val intAdd = intFn { a -> intFn { b -> a + b } }
val listAt = listFn { list -> intFn { index -> list[index] } }

val switch = listFn { caseList ->
	indexedFn { indexed ->
		caseList[indexed.index] eat indexed.value
	}
}

fun switch(vararg cases: X) = switch.eatList(*cases)