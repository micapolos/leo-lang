package leo14.code

data class Code(val string: String) {
	override fun toString() = string
}

val String.code get() = Code(this)