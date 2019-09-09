package leo13

data class ChoiceLink(val choice: Choice, val either: Either)

infix fun Choice.linkTo(either: Either) = ChoiceLink(this, either)

fun ChoiceLink.matches(word: Word): Boolean =
	either.matches(word) || choice.matches(word)

fun ChoiceLink.matches(line: SentenceLine): Boolean =
	either.matches(line) || choice.matches(line)

val ChoiceLink.bodyScript: SentenceScript
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

fun ChoiceLink.contains(word: Word): Boolean =
	either.contains(word) || choice.contains(word)

fun ChoiceLink.contains(line: PatternLine): Boolean =
	either.contains(line) || choice.contains(line)