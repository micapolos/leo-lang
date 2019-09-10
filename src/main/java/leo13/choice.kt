package leo13

import leo.base.fold

data class Choice(val linkOrNull: ChoiceLink?)

fun choice(linkOrNull: ChoiceLink?) = Choice(linkOrNull)
fun Choice.plus(either: Either) = choice(linkTo(either))
fun choice(vararg eithers: Either) = choice(null).fold(eithers) { plus(it) }

fun Choice.matches(word: Word): Boolean =
	linkOrNull != null && linkOrNull.matches(word)

fun Choice.matches(sentence: Sentence): Boolean =
	sentence is LineSentence && matches(sentence.line)

fun Choice.matches(line: SentenceLine): Boolean =
	linkOrNull != null && linkOrNull.matches(line)

val Choice.sentenceLine: SentenceLine
	get() =
		choiceWord lineTo bodySentence

val Choice.bodyScript: SentenceOption
	get() =
		linkOrNull?.bodyScript ?: sentenceOption()

val Choice.bodySentence: Sentence
	get() =
		bodyScript.sentenceOrNull ?: sentence(noneWord)

val Choice.sentence: Sentence
	get() =
		sentence(sentenceLine)

val SentenceLine.failableChoice: Failable<Choice>
	get() =
		failableSentence(choiceWord).failableMap { failableBodyChoice }

val Sentence.failableBodyChoice: Failable<Choice>
	get() =
		when (this) {
			is WordSentence -> word.failableUnit(noneWord).map(choiceWord) { choice() }
			is LineSentence -> line.failableEither.map(choiceWord) { choice(this) }
			is LinkSentence -> link.failableChoiceLink.map(choiceWord) { choice(this) }
		}

fun Choice.contains(pattern: Pattern): Boolean =
	when (pattern) {
		is WordPattern -> contains(pattern.word)
		is LinePattern -> contains(pattern.line)
		is LinkPattern -> false
		is ChoicePattern -> this == pattern.choice
		is SentencePattern -> false
		is ArrowPattern -> TODO()
	}

fun Choice.contains(word: Word): Boolean =
	linkOrNull?.contains(word) ?: false

fun Choice.contains(line: PatternLine): Boolean =
	linkOrNull?.contains(line) ?: false