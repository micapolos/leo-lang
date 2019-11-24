package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo14.Number
import leo14.number
import java.math.BigDecimal

sealed class NumberParser
object BeginNumberParser : NumberParser()
object NegativeNumberParser : NumberParser()
data class WholeNumberParser(val whole: BigDecimal, val sign: BigDecimal, val string: String) : NumberParser()
data class WholeDotNumberParser(val whole: BigDecimal, val sign: BigDecimal, val string: String) : NumberParser()
data class FullNumberParser(val whole: BigDecimal, val fraction: BigDecimal, val sign: BigDecimal, val string: String) : NumberParser()

val emptyNumberParser: NumberParser = BeginNumberParser

val NumberParser.numberOrNull: Number?
	get() =
		when (this) {
			is BeginNumberParser -> null
			is NegativeNumberParser -> null
			is WholeNumberParser -> number(whole * sign)
			is WholeDotNumberParser -> number(whole * sign)
			is FullNumberParser -> number(whole * sign)
		}

val NumberParser.number get() = numberOrNull!!

fun NumberParser.parse(char: Char): NumberParser? =
	when (this) {
		is BeginNumberParser ->
			when (char) {
				'-' -> NegativeNumberParser
				else -> char.digitBigDecimalOrNull?.let {
					WholeNumberParser(it, sign = BigDecimal.ONE, string = "$char")
				}
			}
		is NegativeNumberParser ->
			char.digitBigDecimalOrNull?.let {
				WholeNumberParser(it, sign = BigDecimal.ONE.negate(), string = "-$char")
			}
		is WholeNumberParser ->
			if (char == '.') WholeDotNumberParser(whole, sign, "$string.")
			else char.digitBigDecimalOrNull?.let {
				copy(whole = whole * BigDecimal.TEN + it, string = "$string$char")
			}
		is WholeDotNumberParser ->
			char.digitBigDecimalOrNull?.let {
				FullNumberParser(whole + it * BigDecimal("0.1"), BigDecimal("0.01"), sign, string = "$string$char")
			}
		is FullNumberParser ->
			char.digitBigDecimalOrNull?.let { digitInt ->
				copy(
					whole = whole + digitInt * fraction,
					fraction = fraction * BigDecimal("0.1"),
					string = "$string$char")
			}
	}

val Char.digitIntOrNull: Int?
	get() =
		notNullIf(isDigit()) {
			minus('0')
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

val Char.digitBigDecimalOrNull
	get() =
		digitIntOrNull?.let { BigDecimal.valueOf(it.toLong()) }