package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo.base.orNull
import leo13.*
import leo14.line
import leo14.lineTo
import leo14.literal
import leo14.script

sealed class StringParser
object BeginStringParser : StringParser()
data class CharStackStringParser(val charStack: Stack<Char>) : StringParser()
data class EndStringParser(val string: String) : StringParser()

val emptyStringParser: StringParser = BeginStringParser
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
	emptyStringParser
		.parse(string)!!
		.string

val StringParser.coreString
	get() =
		when (this) {
			is BeginStringParser -> ""
			is CharStackStringParser -> "\"" + StringBuilder().fold(charStack.reverse) { append(it) }.toString()
			is EndStringParser -> "\"" + string + "\""
		}

val StringParser.reflectScriptLine
	get() =
		"string" lineTo script(
			"parser" lineTo script(
				when (this) {
					BeginStringParser -> "empty".line
					is CharStackStringParser -> "open" lineTo script(literal(StringBuilder().fold(charStack.reverse) { append(it) }.toString()))
					is EndStringParser -> line(literal(string))
				}
			)
		)