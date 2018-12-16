package leo.term

data class Rule(
	val matcher: Matcher,
	val body: Body) {
	override fun toString() = "$matcher, $body"
}

infix fun Matcher.gives(body: Body): Rule =
	Rule(this, body)