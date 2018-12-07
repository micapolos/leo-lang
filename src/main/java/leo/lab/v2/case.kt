package leo.lab.v2

import leo.End
import leo.Word

sealed class Case

data class WordCase(
	val word: Word,
	val pattern: Pattern) : Case()

data class EndCase(
	val end: End,
	val match: Match) : Case()

infix fun Word.caseTo(pattern: Pattern): Case =
	WordCase(this, pattern)

infix fun End.caseTo(match: Match): Case =
	EndCase(this, match)
