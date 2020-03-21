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

val Script.spacedString: String
	get() =
		let { script ->
			processorString {
				map(Token::spacedString).process(script)
			}
		}
