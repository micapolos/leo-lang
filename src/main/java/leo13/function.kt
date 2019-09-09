package leo13

data class Function(val pattern: Pattern, val bodySentence: Sentence)

fun function(pattern: Pattern, bodySentence: Sentence) = Function(pattern, bodySentence)

val Sentence.failableFunction: Failable<Function>
	get() =
		TODO()
