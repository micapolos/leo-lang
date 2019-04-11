package leo.base

data class If(val condition: Boolean)
data class Then<out V>(val _if: If, val thenFn: () -> V)

fun _if(condition: Boolean) = If(condition)
fun <V> If._then(thenFn: () -> V) = Then(this, thenFn)
fun <V> Then<V>._else(elseFn: () -> V) = if (_if.condition) thenFn() else elseFn()
