package leo14.parser

import leo.base.fold
import leo.base.ifOrNull
import leo14.*

sealed class SpacedTokenParser
object NewSpacedTokenParser : SpacedTokenParser()
data class TokenSpacedTokenParser(val token: Token) : SpacedTokenParser()
data class LiteralSpacedTokenParser(val literalParser: LiteralParser) : SpacedTokenParser()
data class NameSpacedTokenParser(val nameParser: NameParser) : SpacedTokenParser()

val newSpacedTokenParser: SpacedTokenParser = NewSpacedTokenParser

fun SpacedTokenParser.parse(char: Char): SpacedTokenParser? =
	when (this) {
		is NewSpacedTokenParser ->
			when (char) {
				' ' -> TokenSpacedTokenParser(token(end))
				else -> null
					?: newLiteralParser.parse(char)?.let(::LiteralSpacedTokenParser)
					?: newNameParser.parse(char)?.let(::NameSpacedTokenParser)
			}
		is TokenSpacedTokenParser ->
			null
		is LiteralSpacedTokenParser ->
			literalParser.parse(char)
				?.let(::LiteralSpacedTokenParser)
				?: ifOrNull(char == ' ') {
					literalParser.literalOrNull?.let(::token)?.let(::TokenSpacedTokenParser)
				}
		is NameSpacedTokenParser ->
			when (char) {
				' ' -> nameParser.nameOrNull?.let { TokenSpacedTokenParser(token(begin(it))) }
				else -> nameParser.parse(char)?.let(::NameSpacedTokenParser)
			}
	}

val SpacedTokenParser.canContinue
	get() =
		when (this) {
			is NewSpacedTokenParser -> true
			is TokenSpacedTokenParser -> false
			is LiteralSpacedTokenParser -> false
			is NameSpacedTokenParser -> true
		}

val SpacedTokenParser.tokenOrNull: Token?
	get() =
		when (this) {
			is NewSpacedTokenParser -> null
			is TokenSpacedTokenParser -> token
			is LiteralSpacedTokenParser -> null
			is NameSpacedTokenParser -> null
		}

val String.spacedToken
	get() =
		newSpacedTokenParser.fold(this) { parse(it)!! }.tokenOrNull!!

val SpacedTokenParser.spacedString: String
	get() =
		when (this) {
			is NewSpacedTokenParser -> ""
			is TokenSpacedTokenParser -> token.spacedString
			is LiteralSpacedTokenParser -> literalParser.spacedString
			is NameSpacedTokenParser -> nameParser.spacedString
		}

val SpacedTokenParser.coreString: String
	get() =
		when (this) {
			is NewSpacedTokenParser -> ""
			is TokenSpacedTokenParser -> token.toString()
			is LiteralSpacedTokenParser -> literalParser.coreString
			is NameSpacedTokenParser -> nameParser.coreString
		}

val SpacedTokenParser.reflectScriptLine get() =
	"spaced" lineTo script(
		"token" lineTo script(
			"parser" lineTo script(
				literal("TODO"))))
