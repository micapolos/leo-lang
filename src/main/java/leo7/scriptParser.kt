package leo7

import leo.base.ifNull

data class ScriptParser(
	val tokenScriptParser: TokenScriptParser,
	val partialTokenParser: PartialTokenParser)

val newScriptParser get() = ScriptParser(newTokenScriptParser, newPartialTokenParser)

fun ScriptParser.read(char: Char) =
	partialTokenParser.read(char)?.let { tokenParser ->
		when (tokenParser) {
			is PartialTokenParser -> copy(partialTokenParser = tokenParser)
			is FullTokenParser -> tokenScriptParser.read(tokenParser.token)?.let { tokenScriptParser ->
				copy(tokenScriptParser = tokenScriptParser, partialTokenParser = newPartialTokenParser)
			}
		}
	}

val ScriptParser.parsedScriptOrNull: Script?
	get() =
		partialTokenParser.wordParser.parsedWordOrNull.ifNull {
			tokenScriptParser.parsedScriptOrNull
		}