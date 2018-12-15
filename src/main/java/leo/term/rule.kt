package leo.term

data class Rule(
	val pattern: Pattern,
	val body: Body) {
	override fun toString() = "$pattern, $body"
}

infix fun Pattern.gives(body: Body): Rule =
	Rule(this, body)