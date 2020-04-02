package leo14.untyped.reader

import leo14.*
import leo14.parser.SpacedTokenParser
import leo14.parser.newSpacedTokenParser
import leo14.parser.parse
import leo14.parser.tokenOrNull

data class Reader<T>(
	val reducer: Reducer<T, Token>,
	val block: Block)

data class Block(
	val parentOrNull: Block?,
	val hasSpaces: Boolean,
	val hadComma: Boolean,
	val spacedTokenParser: SpacedTokenParser)

fun <T> Reader<T>.read(char: Char): Reader<T>? =
	when (char) {
		'(' ->
			block.spacedTokenParser.parse(' ')?.tokenOrNull?.let { token ->
				when (token) {
					is LiteralToken -> null
					is BeginToken ->
						Reader(
							reducer.reduce(token),
							Block(
								parentOrNull = block,
								hasSpaces = false,
								hadComma = false,
								spacedTokenParser = newSpacedTokenParser))
					is EndToken -> null
				}
			}
		')' ->
			block.spacedTokenParser.parse(' ')?.tokenOrNull?.let { token ->
				when (token) {
					is LiteralToken -> null
					is BeginToken ->
						Reader(
							reducer.reduce(token),
							Block(
								parentOrNull = block,
								hasSpaces = false,
								hadComma = false,
								spacedTokenParser = newSpacedTokenParser))
					is EndToken -> null
				}
			}
		',' ->
			block.spacedTokenParser.parse(' ')?.tokenOrNull?.let { token ->
				when (token) {
					is LiteralToken ->
						Reader(
							reducer.reduce(token),
							Block(
								parentOrNull = block.parentOrNull,
								hasSpaces = false,
								hadComma = true,
								spacedTokenParser = newSpacedTokenParser))
					is BeginToken ->
						Reader(
							reducer.reduce(token).reduce(token(end)),
							Block(
								parentOrNull = block,
								hasSpaces = false,
								hadComma = true,
								spacedTokenParser = newSpacedTokenParser))
					is EndToken -> null
				}
			}
		' ' ->
			if (block.hasSpaces) this
			else block.spacedTokenParser.parse(' ')?.tokenOrNull?.let { token ->
				when (token) {
					is LiteralToken ->
						Reader(
							reducer.reduce(token),
							Block(
								parentOrNull = block.parentOrNull,
								hasSpaces = false,
								hadComma = true,
								spacedTokenParser = newSpacedTokenParser))
					is BeginToken ->
						Reader(
							reducer.reduce(token).reduce(token(end)),
							Block(
								parentOrNull = block,
								hasSpaces = false,
								hadComma = true,
								spacedTokenParser = newSpacedTokenParser))
					is EndToken -> null
				}
			}
		else ->
			block.spacedTokenParser.parse(char)?.let { parser ->
				Reader(
					reducer,
					block.copy(spacedTokenParser = parser))
			}
	}