package leo.lab.v2

import leo.End
import leo.Word

sealed class Case

data class WordCase(
	val word: Word,
	val function: Function) : Case()

data class EndCase(
	val end: End,
	val match: Match) : Case()

infix fun Word.caseTo(function: Function): Case =
	WordCase(this, function)

infix fun End.caseTo(match: Match): Case =
	EndCase(this, match)

infix fun End.caseTo(recursion: Recursion): Case =
	caseTo(recursion.function.match)

infix fun End.caseTo(template: Template): Case =
	caseTo(template.body.match)
