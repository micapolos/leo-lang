package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo13.*
import leo14.lineTo
import leo14.literal
import leo14.script

data class NameParser(val validCharStack: Stack<Char>)

val newNameParser = NameParser(stack())

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
	newNameParser.fold(string) { parse(it)!! }.nameOrNull!!

val NameParser.coreString
	get() =
		nameOrNull ?: ""

val NameParser.spacedString
	get() =
		nameOrNull ?: ""

val NameParser.reflectScriptLine
	get() =
		"name" lineTo script(
			"parser" lineTo script(
				literal(StringBuilder().fold(validCharStack.reverse) { append(it) }.toString())))