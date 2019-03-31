package leo.base

val ClosedRange<Int>.intWithoutStart
	get() =
		start.inc()..endInclusive

val ClosedRange<Int>.intSize
	get() =
		endInclusive - start + 1
