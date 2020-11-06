package leo21.parser

data class LeadParser(val lead: Lead, val indentParserOrNull: IndentParser?)

val Lead.parser: LeadParser?
	get() =
		LeadParser(this, null)

fun LeadParser.plus(char: Char): LeadParser? {
	val indentParser =
		if (indentParserOrNull == null) char.beginIndentParser
		else indentParserOrNull.plus(char)
	val indentOrNull = indentParser?.end
	return if (indentOrNull == null) copy(indentParserOrNull = indentParser)
	else lead.plus(indent)?.let { copy(lead = it, indentParserOrNull = null) }
}
