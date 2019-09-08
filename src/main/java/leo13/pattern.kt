package leo13

import leo.base.fold

sealed class Pattern

data class WordPattern(val word: Word) : Pattern()
data class LinePattern(val line: PatternLine) : Pattern()
data class LinkPattern(val link: PatternLink) : Pattern()
data class ChoicePattern(val choice: Choice) : Pattern()
data class SentencePattern(val sentence: ObjectSentence) : Pattern()
data class ArrowPattern(val arrow: PatternArrow) : Pattern()

fun pattern(word: Word): Pattern = WordPattern(word)
fun pattern(line: PatternLine): Pattern = LinePattern(line)
fun pattern(link: PatternLink): Pattern = LinkPattern(link)
fun pattern(choice: Choice): Pattern = ChoicePattern(choice)
fun pattern(sentence: ObjectSentence): Pattern = SentencePattern(sentence)
fun pattern(arrow: PatternArrow): Pattern = ArrowPattern(arrow)

fun Pattern.plus(line: PatternLine): Pattern = pattern(linkTo(line))

fun pattern(word: Word, vararg lines: PatternLine) = pattern(word).fold(lines) { plus(it) }
fun pattern(line: PatternLine, vararg lines: PatternLine) = pattern(line).fold(lines) { plus(it) }
fun pattern(choice: Choice, vararg lines: PatternLine) = pattern(choice).fold(lines) { plus(it) }
fun pattern(sentence: ObjectSentence, vararg lines: PatternLine) = pattern(sentence).fold(lines) { plus(it) }
fun pattern(arrow: PatternArrow, vararg lines: PatternLine) = pattern(arrow).fold(lines) { plus(it) }

fun Pattern.matches(sentence: Sentence): Boolean =
	when (this) {
		is WordPattern -> sentence is WordSentence && word == sentence.word
		is LinePattern -> sentence is LineSentence && line.matches(sentence.line)
		is LinkPattern -> sentence is LinkSentence && link.matches(sentence.link)
		is ChoicePattern -> choice.matches(sentence)
		is SentencePattern -> true
		is ArrowPattern -> arrow.matches(sentence)
	}

val Pattern.sentenceLine: SentenceLine
	get() =
		patternWord lineTo bodySentence

val Pattern.bodySentence: Sentence
	get() =
		when (this) {
			is WordPattern -> sentence(word) // TODO: handle meta
			is LinePattern -> line.bodySentence // TODO: handle meta
			is LinkPattern -> link.bodySentence
			is ChoicePattern -> choice.sentence
			is SentencePattern -> sentence.sentence
			is ArrowPattern -> TODO()
		}

val SentenceLine.failablePattern: Failable<Pattern>
	get() =
		failableSentence(patternWord).failableMap(patternWord) { failableBodyPattern }

val Sentence.failableBodyPattern: Failable<Pattern>
	get() =
		when (this) {
			is WordSentence -> word.failableBodyPattern
			is LineSentence -> line.failableBodyPattern
			is LinkSentence -> link.failableBodyPattern
		}

val Word.failableBodyPattern: Failable<Pattern>
	get() =
		when (this) {
			sentenceWord -> success(pattern(sentence))
			else -> success(pattern(this))
		}

val SentenceLine.failableBodyPattern: Failable<Pattern>
	get() =
		when (word) {
			choiceWord -> sentence.failableBodyChoice.map { pattern(this) }
			sentenceWord -> sentence.failableWord.map { pattern(leo13.sentence) }
			else -> failableBodyPatternLine.map { pattern(this) }
		}

val SentenceLink.failableBodyPattern: Failable<Pattern>
	get() =
		failableBodyPatternLink.map { pattern(this) }
