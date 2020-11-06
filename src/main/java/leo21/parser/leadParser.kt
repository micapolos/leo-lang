package leo21.parser

data class LeadParser(val lead: Lead, val indentParserOrNull: IndentParser?)

val Lead.parser: LeadParser?
	get() =
		LeadParser(this, null)

fun LeadParser.plus(char: Char): LeadParser? = TODO()