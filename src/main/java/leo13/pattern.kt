package leo13

import leo.base.fold

sealed class Pattern {
	override fun toString() = sentenceLine.toString()
}

data class WordPattern(val word: Word) : Pattern() {
	override fun toString() = super.toString()
}

data class LinePattern(val line: PatternLine) : Pattern() {
	override fun toString() = super.toString()
}

data class LinkPattern(val link: PatternLink) : Pattern() {
	override fun toString() = super.toString()
}

data class ChoicePattern(val choice: Choice) : Pattern() {
	override fun toString() = super.toString()
}

data class SentencePattern(val sentence: ObjectSentence) : Pattern() {
	override fun toString() = super.toString()
}

data class ArrowPattern(val arrow: PatternArrow) : Pattern() {
	override fun toString() = super.toString()
}

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

val Pattern.lineOrNull: PatternLine? get() = (this as? LinePattern)?.line

val Pattern.failableLine: Failable<PatternLine>
	get() =
		if (this is LinePattern) success(line)
		else failure(expectedWord lineTo sentence(lineWord))

fun Pattern.matches(sentence: Sentence): Boolean =
	when (this) {
		is WordPattern -> sentence is StartSentence && sentence.start is WordSentenceStart && word == sentence.start.word
		is LinePattern -> sentence is StartSentence && sentence.start is LineSentenceStart && line.matches(sentence.start.line)
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
			is StartSentence -> start.failableBodyPattern
			is LinkSentence -> link.failableBodyPattern
		}

val SentenceStart.failableBodyPattern: Failable<Pattern>
	get() =
		when (this) {
			is WordSentenceStart -> word.failableBodyPattern
			is LineSentenceStart -> line.failableBodyPattern
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

fun Pattern.linePatternOrNull(word: Word): Pattern? =
	when (this) {
		is WordPattern -> null
		is LinePattern -> line.patternOrNull(word)
		is LinkPattern -> link.linePatternOrNull(word)
		is ChoicePattern -> null
		is SentencePattern -> null
		is ArrowPattern -> null
	}

fun Pattern.replaceOrNull(newLine: PatternLine): Pattern? =
	when (this) {
		is WordPattern -> null
		is LinePattern -> line.replaceOrNull(newLine)?.let { pattern(it) }
		is LinkPattern -> link.replaceOrNull(newLine)?.let { pattern(it) }
		is ChoicePattern -> null
		is SentencePattern -> null
		is ArrowPattern -> null
	}

fun Pattern.replaceOrNull(pattern: Pattern): Pattern? =
	when (pattern) {
		is WordPattern -> null
		is LinePattern -> replaceOrNull(pattern.line)
		is LinkPattern -> replaceOrNull(pattern.link)
		is ChoicePattern -> null
		is SentencePattern -> null
		is ArrowPattern -> null
	}

fun Pattern.replaceOrNull(link: PatternLink): Pattern? =
	replaceOrNull(link.pattern)?.replaceOrNull(link.line)

fun Pattern.getOrNull(word: Word): Pattern? =
	lineOrNull
		?.pattern
		?.linePatternOrNull(word)
		?.let { pattern(word lineTo it) }

fun Pattern.setOrNull(pattern: Pattern): Pattern? =
	lineOrNull?.let { line ->
		line.pattern.replaceOrNull(pattern)?.let {
			pattern(line.word lineTo it)
		}
	}

fun pattern(sentence: Sentence): Pattern =
	when (sentence) {
		is StartSentence -> pattern(sentence.start)
		is LinkSentence -> pattern(patternLink(sentence.link))
	}

fun pattern(start: SentenceStart): Pattern =
	when (start) {
		is WordSentenceStart -> pattern(start.word)
		is LineSentenceStart -> pattern(patternLine(start.line))
	}

fun Pattern.contains(pattern: Pattern): Boolean =
	when (this) {
		is WordPattern -> pattern is WordPattern && word == pattern.word
		is LinePattern -> pattern is LinePattern && line.contains(pattern.line)
		is LinkPattern -> pattern is LinkPattern && link == pattern.link
		is ChoicePattern -> choice.contains(pattern)
		is SentencePattern -> true
		is ArrowPattern -> TODO()
	}