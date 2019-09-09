package leo13

import leo.base.notNullIf

data class PatternLine(val word: Word, val pattern: Pattern)

infix fun Word.lineTo(pattern: Pattern) = PatternLine(this, pattern)

fun PatternLine.matches(line: SentenceLine): Boolean =
	word == line.word && pattern.matches(line.sentence)

val PatternLine.bodySentenceLine: SentenceLine
	get() =
		word lineTo pattern.bodySentence

val PatternLine.bodySentence: Sentence
	get() =
		sentence(bodySentenceLine)

val SentenceLine.failableBodyPatternLine: Failable<PatternLine>
	get() =
		sentence.failableBodyPattern.map { word lineTo this }

fun PatternLine.patternOrNull(word: Word): Pattern? =
	notNullIf(this.word == word) { pattern }

fun PatternLine.replaceOrNull(newLine: PatternLine): PatternLine? =
	notNullIf(word == newLine.word) { newLine }

fun patternLine(line: SentenceLine): PatternLine =
	line.word lineTo pattern(line.sentence)

fun PatternLine.contains(line: PatternLine): Boolean =
	word == line.word && pattern.contains(line.pattern)