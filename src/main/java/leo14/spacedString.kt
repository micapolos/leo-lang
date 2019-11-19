package leo14

val Token.spacedString
	get() =
		when (this) {
			is LiteralToken -> literal.spacedString
			is BeginToken -> begin.spacedString
			is EndToken -> end.spacedString
		}

val Literal.spacedString get() = toString()
val Begin.spacedString get() = "$string "
val End.spacedString get() = " "

val Script.spacedString
	get() =
		let { script ->
			processorString {
				map<String, Token> { it.spacedString }.process(script)
			}
		}
