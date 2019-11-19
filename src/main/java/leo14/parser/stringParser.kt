package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo.base.orNull
import leo13.*

sealed class StringParser
object BeginStringParser : StringParser()
data class CharStackStringParser(val charStack: Stack<Char>) : StringParser()
data class EndStringParser(val string: String) : StringParser()

val stringParser: StringParser = BeginStringParser
fun stringParser(charStack: Stack<Char>): StringParser = CharStackStringParser(charStack)
fun stringParser(string: String): StringParser = EndStringParser(string)

fun StringParser.parse(char: Char): StringParser? =
	when (this) {
		is BeginStringParser ->
			notNullIf(char == '"') {
				stringParser(stack())
			}
		is CharStackStringParser ->
			when (char) {
				'"' -> stringParser(StringBuilder().fold(charStack.reverse) { append(it) }.toString())
				else -> stringParser(charStack.push(char))
			}
		is EndStringParser -> null
	}

fun StringParser.parse(string: String): StringParser? =
	orNull.fold(string) { this?.parse(it) }

val StringParser.stringOrNull get() = (this as? EndStringParser)?.string

val StringParser.string get() = (this as EndStringParser).string

fun parseString(string: String): String =
	stringParser
		.parse(string)!!
		.string

val StringParser.coreString
	get() =
		when (this) {
			is BeginStringParser -> ""
			is CharStackStringParser -> "\"" + StringBuilder().fold(charStack.reverse) { append(it) }.toString()
			is EndStringParser -> "\"" + string + "\""
		}