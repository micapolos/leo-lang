@file:Suppress("UNCHECKED_CAST")

package leo13.runtime

import leo.base.indexed

typealias X = Any?
typealias Fn = (X) -> X

object Eval
val eval = Eval

fun fn(fn: Fn): X = fn
fun lazy(fn: () -> X) = fn { (it as Eval).run { fn() } }
fun intFn(fn: (Int) -> X) = fn { fn(it as Int) }
fun pairFn(fn: (Pair<X, X>) -> X) = fn { fn(it as Pair<X, X>) }
fun indexedFn(fn: (IndexedValue<X>) -> X) = fn { fn(it as IndexedValue<X>) }
fun arrayFn(fn: (Array<X>) -> X) = fn { fn(it as Array<X>) }

infix fun Any?.eat(any: X) = (this as Fn).invoke(any)
infix fun X.perform(fn: X) = fn eat this
fun Any?.eatArray(vararg any: X) = eat(any)
infix fun Any?.dot(any: X) = fn { eat(any.eat(it)) }

val intInc = intFn { it.inc() }
val intDec = intFn { it.dec() }
val intAdd = intFn { a -> intFn { b -> a + b } }
val arrayAt = arrayFn { array -> intFn { index -> array[index] } }

val switch = arrayFn { cases ->
	indexedFn { indexed ->
		cases[indexed.index] eat indexed.value
	}
}

fun switch(vararg cases: X) = switch eat cases

fun X.switch(vararg cases: X) = switch eat cases eat this

val link = fn { tail -> fn { head -> tail to head } }
val tail = pairFn { it.first }
val head = pairFn { it.second }
val empty = 0 indexed null
val append = fn { tail -> fn { head -> 1 indexed (link eat tail eat head) } }
infix fun X.put(x: X) = append eat this eat x
val isEmpty = switch(fn { true }, fn { false })

val size: X
	get() = switch(
		fn { 0 },
		pairFn { link -> size eat link.first perform intInc })