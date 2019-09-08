package leo13

import leo.base.fold

data class Choice(val linkOrNull: ChoiceLink?)

fun choice(linkOrNull: ChoiceLink?) = Choice(linkOrNull)
fun Choice.plus(either: Either) = choice(linkTo(either))
fun choice(vararg eithers: Either) = choice(null).fold(eithers) { plus(it) }

fun Choice.matches(sentence: Sentence): Boolean =
	sentence is LineSentence && matches(sentence.line)

fun Choice.matches(line: SentenceLine): Boolean =
	linkOrNull != null && linkOrNull.matches(line)

val Choice.sentenceLine: SentenceLine
	get() =
		choiceWord lineTo bodySentence

val Choice.bodyScript: SentenceScript
	get() =
		linkOrNull?.bodyScript ?: script()

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