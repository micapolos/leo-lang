package leo.lab.v2

sealed class Getter

object LhsGetter : Getter()

object RhsGetter : Getter()

val lhsGetter: Getter
	get() =
		LhsGetter

val rhsGetter: Getter
	get() =
		RhsGetter

fun Script.get(getter: Getter): Script? =
	when (getter) {
		is LhsGetter -> lhsOrNull
		is RhsGetter -> rhsOrNull
	}

//val Getter.reflect: Field<Nothing>
//	get() =
//		getWord fieldTo when (this) {
//			is WordGetter -> word.reflect.term
//			is LastGetter -> topWord.term
//			is PreviousGetter -> popWord.term
//		}
