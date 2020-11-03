package leo14.lambda.scheme

data class Code(val string: String) {
	override fun toString() = string
}

fun code(string: String): Code = Code(string)

infix fun Code.ret(body: Code): Code =
	code("(lambda ($this) $body)")

infix fun Code.ap(rhs: Code): Code =
	code("($this $rhs)")
