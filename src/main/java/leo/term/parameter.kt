package leo.term

data class Parameter<out V>(
	val termOrNull: Term<V>?) {
	override fun toString() =
		when {
			termOrNull == null -> ""
			termOrNull.isSimple -> " $termOrNull"
			else -> "($termOrNull)"
		}
}

val <V> Term<V>?.parameter: Parameter<V>
	get() =
		Parameter(this)

fun <V> parameter(termOrNull: Term<V>?): Parameter<V> =
	termOrNull.parameter