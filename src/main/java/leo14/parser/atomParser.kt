package leo14.parser

import leo.base.fold
import leo.base.orNull
import leo13.Empty
import leo13.empty
import leo14.Atom
import leo14.atom

sealed class AtomParser

data class EmptyAtomParser(val empty: Empty) : AtomParser()
data class LiteralParserAtomParser(val literalParser: LiteralParser) : AtomParser()
data class NameParserAtomParser(val nameParser: NameParser) : AtomParser()

fun atomParser(empty: Empty): AtomParser =
	EmptyAtomParser(empty)

fun AtomParser.parse(char: Char): AtomParser? =
	when (this) {
		is EmptyAtomParser ->
			null
				?: newLiteralParser.parse(char)?.let(::LiteralParserAtomParser)
				?: newNameParser.parse(char)?.let(::NameParserAtomParser)
		is LiteralParserAtomParser ->
			literalParser.parse(char)?.let(::LiteralParserAtomParser)
		is NameParserAtomParser ->
			nameParser.parse(char)?.let(::NameParserAtomParser)
	}

val AtomParser.atomOrNull: Atom?
	get() =
		when (this) {
			is EmptyAtomParser -> null
			is LiteralParserAtomParser -> literalParser.literalOrNull?.let(::atom)
			is NameParserAtomParser -> nameParser.nameOrNull?.let(::atom)
		}

val String.atomOrNull: Atom?
	get() =
		atomParser(empty).orNull.fold(this) { this?.parse(it) }?.atomOrNull