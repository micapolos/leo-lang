package leo.term

data class Rule(
	val type: Type,
	val body: Body) {
	override fun toString() = "$type, $body"
}

infix fun Type.gives(body: Body): Rule =
	Rule(this, body)