package leo13

import leo13.generic.*
import leo13.generic.List

data class WordChoice(val eitherList: List<WordEither>)

fun wordChoice(eitherList: List<WordEither>) =
	WordChoice(eitherList)

fun choice(either: WordEither, vararg eithers: WordEither) =
	WordChoice(list(either, *eithers))

infix fun WordChoice.plus(either: WordEither) =
	WordChoice(eitherList.plus(either))

val wordChoiceSentenceStartWriter: SentenceStartWriter<WordChoice> =
	startWriter(listSentenceWriter(choiceWord, wordEitherSentenceWriter).map { eitherList })

fun WordChoice.matches(word: Word): Boolean =
	eitherList.any { matches(word) }

fun WordChoice.contains(choice: WordChoice): Boolean =
	choice
		.onlyEitherOrNull
		?.let { contains(it) }
		?: this == choice

fun WordChoice.contains(either: WordEither): Boolean =
	eitherList.any { contains(either) }

val WordChoice.onlyEitherOrNull: WordEither?
	get() =
		eitherList.onlyHeadOrNull
