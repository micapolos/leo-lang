package leo16

import leo.ansi
import leo.defaultColor
import leo.magenta
import leo.reset
import leo14.Literal
import leo14.Number
import leo14.NumberLiteral
import leo14.StringLiteral
import leo16.names.*
import java.math.BigDecimal

val Literal.sentence: Sentence
	get() =
		when (this) {
			is StringLiteral -> string.sentence
			is NumberLiteral -> number.sentence
		}

val String.sentence: Sentence
	get() =
		_text(nativeValue)

val Number.sentence: Sentence
	get() =
		bigDecimal.sentence

val BigDecimal.sentence: Sentence
	get() =
		_number(nativeValue)

val Int.sentence: Sentence
	get() =
		_int(nativeValue)

val Any?.nativeString: String
	get() =
		"${ansi.magenta}«$this»${ansi.defaultColor}"

fun <R : Any> nullIfThrowsException(fn: () -> R): R? =
	try {
		fn()
	} catch (e: Exception) {
		null
	}
