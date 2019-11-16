package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo13.*

data class NameParser(val validCharStack: Stack<Char>)

val nameParser = NameParser(stack())

fun NameParser.parse(char: Char): NameParser? =
	notNullIf(char.isLetter() && char.isLowerCase()) {
		NameParser(validCharStack.push(char))
	}

val NameParser.nameOrNull: String?
	get() =
		notNullIf(!validCharStack.isEmpty) {
			StringBuilder().fold(validCharStack.reverse) { append(it) }.toString()
		}

val NameParser.isEmpty
	get() =
		validCharStack.isEmpty

fun parseName(string: String): String =
	nameParser.fold(string) { parse(it)!! }.nameOrNull!!