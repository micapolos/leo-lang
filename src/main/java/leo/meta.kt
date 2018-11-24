package leo

data class Meta<out V>(
	val value: V)

val <V> V.meta: Meta<V>
	get() =
		Meta(this)
