package leo9

import leo.base.Empty

sealed class Term

data class EmptyTerm(
	val empty: Empty) : Term()

data class FunctionTerm(
	val function: Function) : Term()

data class LinkTerm(
	val link: TermLink) : Term()

data class TermLink(
	val term: Term,
	val line: TermLine)

data class TermLine(
	val name: String,
	val term: Term)
