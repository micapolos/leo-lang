package leo16.term

import leo.stak.Stak
import leo.stak.emptyStak
import leo.stak.push
import leo.stak.top

data class Scope<out V>(val thunkStak: Stak<Thunk<V>>)

fun <V> emptyScope(): Scope<V> = Scope(emptyStak())
operator fun <V> Scope<V>.plus(thunk: Thunk<V>): Scope<V> = Scope(thunkStak.push(thunk))
operator fun <V> Scope<V>.get(index: Int): Thunk<V> = thunkStak.top(index)!!

