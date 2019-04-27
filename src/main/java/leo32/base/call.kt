package leo32.base

data class Call<V, R>(
	val value: V,
	val fn: R.() -> V)

fun <V, R> call(value: V, fn: R.() -> V) = Call(value, fn)