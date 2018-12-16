package leo.term

data class Receiver<out V>(
	val termOrNull: Term<V>?) {
	override fun toString() = if (termOrNull != null) "$termOrNull, " else ""
}

val <V> Term<V>?.receiver: Receiver<V>
	get() =
		Receiver(this)

fun <V> receiver(termOrNull: Term<V>?): Receiver<V> =
	termOrNull.receiver
