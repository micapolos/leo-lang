package leo21.parser

import leo.base.notNullIf
import leo13.StackLink
import leo13.asStack
import leo13.charString
import leo13.push
import leo13.stackLink

data class NameParser(val charStackLink: StackLink<Char>)

val Char.beginNameParser: NameParser?
	get() =
		notNullIf(isLetter()) {
			NameParser(stackLink(this))
		}

fun NameParser.plus(char: Char): NameParser? =
	notNullIf(char.isLetter()) {
		NameParser(charStackLink.push(char))
	}

val NameParser.end: String?
	get() =
		charStackLink.asStack.charString
