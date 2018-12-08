package leo.lab.v2

data class TraceLink(
	val jump: Jump,
	val trace: Trace)

data class Trace(
	val pattern: Pattern,
	val traceLinkOrNull: TraceLink?)

val nullTraceLink: TraceLink? = null

fun trace(pattern: Pattern): Trace =
	Trace(pattern, null)

fun Trace.plus(jump: Jump): TraceLink =
	TraceLink(jump, this)

fun TraceLink?.plus(pattern: Pattern): Trace =
	Trace(pattern, this)

val Trace.siblingOrNull: Trace?
	get() =
		traceLinkOrNull?.siblingTraceOrNull

val Trace.parentOrNull: Trace?
	get() =
		traceLinkOrNull?.parentTraceOrNull

val TraceLink.siblingTraceOrNull: Trace?
	get() =
		when (jump) {
			is SiblingJump -> trace
			is ParentJump -> null
		}

val TraceLink.parentTraceOrNull: Trace?
	get() =
		when (jump) {
			is SiblingJump -> trace.traceLinkOrNull?.parentTraceOrNull
			is ParentJump -> trace
		}
