package leo16.processor

import leo13.StackLink
import leo13.asStack
import leo13.linkTo
import leo13.stack

data class Word(val letterStackLink: StackLink<Char>)

val StackLink<Char>.word get() = Word(this)
fun Word?.plusOrNull(char: Char): Word? =
	if (!char.isLetter()) null
	else (if (this == null) stack() else letterStackLink.asStack).linkTo(char).word