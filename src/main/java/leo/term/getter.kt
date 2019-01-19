package leo.term

sealed class Getter

object LhsGetter : Getter() {
	override fun toString() = "get receiver"
}

object RhsGetter : Getter() {
	override fun toString() = "get argument"
}

val lhsGetter: Getter
	get() =
		LhsGetter

val rhsGetter: Getter
	get() =
		RhsGetter

fun <V> Term<V>.get(getter: Getter): Term<V>? =
	when (this) {
		is ValueTerm -> null
		is ApplicationTerm -> when (getter) {
			is LhsGetter -> subject.termOrNull
			is RhsGetter -> application.parameter.termOrNull
		}
	}
