package leo.term

data class Rule(
	val expander: Expander,
	val body: Body) {
	override fun toString() = "$expander, $body"
}

infix fun Expander.gives(body: Body): Rule =
	Rule(this, body)