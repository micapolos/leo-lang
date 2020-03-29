package leo14.parser

import leo.base.fold
import leo.base.notNullIf
import leo.base.orNull
import leo13.*
import leo14.*

val escapeChar: Char = '\\'

sealed class StringParser
object BeginStringParser : StringParser()
data class CharStackStringParser(val charStack: Stack<Char>) : StringParser()
data class EscapeCharStringParser(
	val parent: CharStackStringParser,
	val escapeCharParser: EscapeCharParser) : StringParser()

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
				escapeChar -> EscapeCharStringParser(this, EmptyEscapeCharParser(leo.base.empty))
				'"' -> stringParser(StringBuilder().fold(charStack.reverse) { append(it) }.toString())
				else -> stringParser(charStack.push(char))
			}
		is EscapeCharStringParser ->
			escapeCharParser.parse(char)?.let { parsedEscapeCharParser ->
				parsedEscapeCharParser.charOrNull?.let { escapedChar ->
					stringParser(parent.charStack.push(escapedChar))
				}
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

val StringParser.coreString: String
	get() =
		when (this) {
			is BeginStringParser -> ""
			is CharStackStringParser -> "\"" + StringBuilder().fold(charStack.reverse) { append(it.escapedString) }.toString()
			is EndStringParser -> string.literalString
			is EscapeCharStringParser -> parent.coreString + "$escapeChar"
		}

val StringParser.reflectScriptLine: ScriptLine
	get() =
		"string" lineTo script(
			"parser" lineTo script(
				when (this) {
					BeginStringParser -> "empty".line
					is CharStackStringParser -> "open" lineTo script(literal(StringBuilder().fold(charStack.reverse) { append(it) }.toString()))
					is EndStringParser -> line(literal(string))
					is EscapeCharStringParser -> "escape" lineTo script(parent.reflectScriptLine)
				}
			)
		)