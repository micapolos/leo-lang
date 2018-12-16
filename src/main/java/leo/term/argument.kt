package leo.term

data class Argument<out V>(
	val termOrNull: Term<V>?) {
	override fun toString() =
		when {
			termOrNull == null -> ""
			termOrNull.isSimple -> " $termOrNull"
			else -> "($termOrNull)"
		}
}

val <V> Term<V>?.argument: Argument<V>
	get() =
		Argument(this)

fun <V> argument(termOrNull: Term<V>?): Argument<V> =
	termOrNull.argument