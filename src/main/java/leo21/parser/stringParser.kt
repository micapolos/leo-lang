package leo21.parser

import leo.base.notNullIf
import leo13.Stack
import leo13.charString
import leo13.push
import leo13.stack

data class StringParser(val charStack: Stack<Char>, val isComplete: Boolean)

val Char.beginStringParser: StringParser?
	get() =
		notNullIf(this == '\"') {
			StringParser(stack(), false)
		}

fun StringParser.plus(char: Char): StringParser? =
	notNullIf(!isComplete) {
		if (char == '\"') copy(isComplete = true)
		else copy(charStack = charStack.push(char))
	}

val StringParser.end: String?
	get() =
		notNullIf(isComplete) { charStack.charString }