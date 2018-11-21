package leo.base

fun <V, P> Parse<V, P>?.assertParsedAndRest(parsed: P, vararg rest: V) {
	assertNotNull
	(this!!.parsed to this.streamOrNull?.stack).assertEqualTo(parsed to stackOrNull(*rest))
}
