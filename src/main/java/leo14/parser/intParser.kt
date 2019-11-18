package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import kotlin.math.sign

sealed class IntParser
object BeginIntParser : IntParser()
object NegativeIntParser : IntParser()
data class SignedIntParser(val int: Int, val isNegative: Boolean) : IntParser()

val intParser: IntParser = BeginIntParser

val IntParser.int get() = (this as SignedIntParser).int

fun IntParser.parse(char: Char): IntParser? =
	when (this) {
		is BeginIntParser ->
			when (char) {
				'-' -> NegativeIntParser
				else -> char.digitIntOrNull?.let { SignedIntParser(it, isNegative = false) }
			}
		is NegativeIntParser ->
			char.digitIntOrNull?.let { int ->
				SignedIntParser(-int, isNegative = true)
			}
		is SignedIntParser ->
			char.digitIntOrNull?.let { digitInt ->
				if (int == 0) SignedIntParser(if (isNegative) -digitInt else digitInt, isNegative)
				else (int * 10 + int.sign * digitInt).let { result ->
					notNullIf(result.sign == int.sign) {
						SignedIntParser(result, isNegative)
					}
				}
			}
	}

val Char.digitIntOrNull: Int?
	get() =
		notNullIf(isDigit()) {
			minus('0')
		}

fun parseInt(string: String): Int =
	intParser.fold(string) { parse(it)!! }.int

val IntParser.coreString
	get() =
		when (this) {
			is BeginIntParser -> ""
			is NegativeIntParser -> "-"
			is SignedIntParser -> (if (int == 0 && isNegative) "-" else "") + int.toString()
		}