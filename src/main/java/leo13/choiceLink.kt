package leo13

data class ChoiceLink(val choice: Choice, val either: Either)

infix fun Choice.linkTo(either: Either) = ChoiceLink(this, either)

fun ChoiceLink.matches(line: SentenceLine): Boolean =
	either.matches(line) || choice.matches(line)

val ChoiceLink.bodyScript: Script
	get() =
		choice.bodyScript.plus(either.sentenceLine)

val SentenceLink.failableChoiceLink: Failable<ChoiceLink>
	get() =
		sentence.failableBodyChoice.failableMap {
			let { choice ->
				line.failableEither.map {
					choice.linkTo(this)
				}
			}
		}
