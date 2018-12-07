package leo.lab.v2

data class Trace(
	val pattern: Pattern,
	val previousTraceOrNull: Trace?,
	val parentTraceOrNull: Trace?)

val Pattern.trace: Trace
	get() =
		Trace(this, null, null)

fun Trace.childTrace(pattern: Pattern): Trace =
	Trace(pattern, null, this)

fun Trace.nextTrace(pattern: Pattern): Trace =
	Trace(pattern, this, null)
