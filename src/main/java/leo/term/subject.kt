package leo.term

data class Subject<out V>(
	val termOrNull: Term<V>?) {
	override fun toString() = if (termOrNull != null) "$termOrNull, " else ""
}

val <V> Term<V>?.subject: Subject<V>
	get() =
		Subject(this)

fun <V> subject(termOrNull: Term<V>?): Subject<V> =
	termOrNull.subject
