package leo.term

data class Body(
	val template: Template,
	val function: Function) {
	override fun toString() = "$template, $function"
}