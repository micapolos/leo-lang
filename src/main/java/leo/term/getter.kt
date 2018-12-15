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

fun Script.get(getter: Getter): Script? =
	when (getter) {
		is LhsGetter -> term.receiverOrNull
		is RhsGetter -> term.application.argumentOrNull
	}
