package leo13

import leo13.generic.*
import leo13.generic.List

data class LineChoice(val eitherList: List<LineEither>)

fun lineChoice(eitherList: List<LineEither>) =
	LineChoice(eitherList)

fun choice(either: LineEither, vararg eithers: LineEither) =
	LineChoice(list(either, *eithers))

infix fun LineChoice.plus(line: LineEither) =
	LineChoice(eitherList.plus(line))

val lineChoiceSentenceWriter: SentenceLineWriter<LineChoice> =
	listSentenceWriter(choiceWord, lineEitherSentenceWriter).map { eitherList }

fun LineChoice.matches(line: SentenceLine): Boolean =
	eitherList.any { matches(line) }

fun LineChoice.contains(choice: LineChoice): Boolean =
	choice
		.onlyEitherOrNull
		?.let { contains(it) }
		?: this == choice

fun LineChoice.contains(either: LineEither): Boolean =
	eitherList.any { contains(either) }

val LineChoice.onlyEitherOrNull: LineEither?
	get() =
		eitherList.onlyHeadOrNull
