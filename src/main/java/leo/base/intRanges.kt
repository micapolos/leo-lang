package leo.base

val IntRange.withoutFirst
	get() =
		first.inc()..endInclusive