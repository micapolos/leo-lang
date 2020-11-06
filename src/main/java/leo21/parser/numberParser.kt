package leo21.parser

import leo14.Number
import leo14.digitOrNull
import leo14.int
import leo14.number
import leo14.plus
import leo14.times

data class NumberParser(val number: Number)

val Char.beginNumberParser: NumberParser?
	get() =
		digitOrNull?.let { digit ->
			NumberParser(digit.int.number)
		}

fun NumberParser.plus(char: Char): NumberParser? =
	char.digitOrNull?.let { digit ->
		NumberParser(number.times(10.number).plus(digit.int.number))
	}

val NumberParser.end: Number? get() = number
