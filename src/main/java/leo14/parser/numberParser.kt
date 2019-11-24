package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo14.Number
import leo14.number
import java.math.BigDecimal

sealed class NumberParser
object BeginNumberParser : NumberParser()
object NegativeNumberParser : NumberParser()
data class WholeNumberParser(val string: String) : NumberParser()
data class WholeDotNumberParser(val string: String) : NumberParser()
data class FullNumberParser(val string: String) : NumberParser()

val emptyNumberParser: NumberParser = BeginNumberParser

val NumberParser.numberOrNull: Number?
	get() =
		when (this) {
			is BeginNumberParser -> null
			is NegativeNumberParser -> null
			is WholeNumberParser -> number(BigDecimal(string))
			is WholeDotNumberParser -> number(BigDecimal(string))
			is FullNumberParser -> number(BigDecimal(string))
		}

val NumberParser.number get() = numberOrNull!!

fun NumberParser.parse(char: Char): NumberParser? =
	when (this) {
		is BeginNumberParser ->
			when (char) {
				'-' -> NegativeNumberParser
				else -> notNullIf(char.isDigit()) { WholeNumberParser("$char") }
			}
		is NegativeNumberParser ->
			notNullIf(char.isDigit()) {
				WholeNumberParser("-$char")
			}
		is WholeNumberParser ->
			if (char == '.') WholeDotNumberParser("$string.")
			else notNullIf(char.isDigit()) {
				WholeNumberParser("$string$char")
			}
		is WholeDotNumberParser ->
			notNullIf(char.isDigit()) {
				FullNumberParser("$string$char")
			}
		is FullNumberParser ->
			notNullIf(char.isDigit()) {
				FullNumberParser("$string$char")
			}
	}

fun parseNumber(string: String): Number =
	emptyNumberParser.fold(string) { parse(it)!! }.number

val NumberParser.coreString
	get() =
		when (this) {
			BeginNumberParser -> ""
			NegativeNumberParser -> "-"
			is WholeNumberParser -> string
			is WholeDotNumberParser -> string
			is FullNumberParser -> string
		}
