package leo.term

sealed class Getter

object LhsGetter : Getter() {
	override fun toString() = "lhs"
}

object RhsGetter : Getter() {
	override fun toString() = "rhs"
}

val lhsGetter: Getter
	get() =
		LhsGetter

val rhsGetter: Getter
	get() =
		RhsGetter

fun Script.get(getter: Getter): Script? =
	when (getter) {
		is LhsGetter -> term.receiverOrNull
		is RhsGetter -> term.application.argumentOrNull
	}
