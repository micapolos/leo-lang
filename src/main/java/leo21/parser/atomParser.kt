package leo21.parser

import leo14.Literal

sealed class AtomParser
data class LiteralAtomParser(val literalParser: LiteralParser) : AtomParser()
data class NameAtomParser(val nameParser: NameParser) : AtomParser()

val Char.beginAtomParser: AtomParser?
	get() =
		null
			?: beginLiteralParser?.let(::LiteralAtomParser)
			?: beginNameParser?.let(::NameAtomParser)

fun AtomParser.plus(char: Char): AtomParser? =
	when (this) {
		is LiteralAtomParser -> literalParser.plus(char)?.let(::LiteralAtomParser)
		is NameAtomParser -> nameParser.plus(char)?.let(::NameAtomParser)
	}

val AtomParser.end: Atom?
	get() =
		when (this) {
			is LiteralAtomParser -> literalParser.end?.let(::LiteralAtom)
			is NameAtomParser -> nameParser.end?.let(::NameAtom)
		}
