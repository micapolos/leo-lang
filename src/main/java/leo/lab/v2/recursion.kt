package leo.lab.v2

sealed class Recursion

data class ParentRecursion(
	val recursionOrNull: Recursion?) : Recursion()

data class PreviousRecursion(
	val recursionOrNull: Recursion?) : Recursion()

val nullRecursion: Recursion? =
	null

val Recursion?.parent: Recursion
	get() =
		ParentRecursion(this)

val Recursion?.previous: Recursion
	get() =
		PreviousRecursion(this)

fun Recursion.apply(traceOrNull: Trace): Trace? =
	when (this) {
		is ParentRecursion -> traceOrNull.parentTraceOrNull?.let { parentTrace ->
			recursionOrNull.orNullApply(parentTrace)
		}
		is PreviousRecursion -> traceOrNull.previousTraceOrNull?.let { previousTrace ->
			recursionOrNull.orNullApply(previousTrace)
		}
	}

fun Recursion?.orNullApply(trace: Trace): Trace? =
	if (this == null) trace
	else apply(trace)
