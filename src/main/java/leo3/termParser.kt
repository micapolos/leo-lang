package leo3

import leo.base.*

data class TermParser(
	val parentOrNull: TermParserParent?,
	val termOrNull: Term?) {
	override fun toString() = appendableString { it.append(this) }
}

data class TermParserParent(
	val termParser: TermParser,
	val begin: Begin) {
	override fun toString() = appendableString { it.append(this) }
}

val Empty.termParser
	get() = TermParser(null, null)

fun TermParser.plus(token: Token): TermParser? =
	when (token) {
		is BeginToken -> TermParser(TermParserParent(this, token.begin), null)
		is EndToken -> parentOrNull?.let { parent ->
			TermParser(
				parent.termParser.parentOrNull,
				parent.termParser.termOrNull.plus(parent.begin.word, termOrNull))
		}
	}

fun TermParser.plus(termOrNull: Term?): TermParser =
	orNullFold(termOrNull.tokenSeq, TermParser::plus)!!

val TermParser.parameterOrNull: Parameter?
	get() = parentOrNull.ifNull { parameter(termOrNull) }

fun Appendable.append(termParser: TermParser) =
	this
		.ifNotNull(termParser.parentOrNull) { append(it) }
		.ifNotNull(termParser.termOrNull) { append(it) }

fun Appendable.append(termParserParent: TermParserParent): Appendable =
	this
		.append(termParserParent.termParser)
		.append(termParserParent.begin)
