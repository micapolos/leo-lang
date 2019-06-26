package leo7

import leo.base.Stack
import leo.base.notNullIf
import leo.base.push

data class ScriptParser(
	val scriptBeginStackOrNull: Stack<ScriptBegin>?,
	val script: Script,
	val partialTokenParser: PartialTokenParser)

val newScriptParser get() = ScriptParser(null, script(), newPartialTokenParser)

fun ScriptParser.read(char: Char) =
	partialTokenParser.read(char)?.let { tokenParser ->
		when (tokenParser) {
			is PartialTokenParser -> copy(partialTokenParser = tokenParser)
			is FullTokenParser -> when (tokenParser.token) {
				is BeginToken -> copy(
					scriptBeginStackOrNull = scriptBeginStackOrNull.push(script.begin(tokenParser.token.begin.word)),
					script = script(),
					partialTokenParser = newPartialTokenParser)
				is EndToken -> scriptBeginStackOrNull?.let { scriptBeginStack ->
					copy(
						scriptBeginStackOrNull = scriptBeginStack.tail,
						script = scriptBeginStack.head.script.plus(scriptBeginStack.head.begin.word lineTo script),
						partialTokenParser = newPartialTokenParser)
				}
			}
		}
	}

val ScriptParser.parsedScriptOrNull
	get() =
		notNullIf(scriptBeginStackOrNull == null && partialTokenParser.parsedTokenOrNull == null) { script }
