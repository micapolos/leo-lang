package leo.lab.v1

sealed class Getter

object LhsGetter : Getter()

object RhsGetter : Getter()

val lhsGetter: Getter
	get() =
		LhsGetter

val rhsGetter: Getter
	get() =
		RhsGetter

fun <V> Script<V>.get(getter: Getter): Script<V>? =
	when (getter) {
		is LhsGetter -> invokeScriptOrNull?.lhsOrNull
		is RhsGetter -> invokeScriptOrNull?.rhsOrNull
	}

//val Getter.reflect: Field<Nothing>
//	get() =
//		getWord fieldTo when (this) {
//			is WordGetter -> word.reflect.term
//			is LastGetter -> topWord.term
//			is PreviousGetter -> popWord.term
//		}
