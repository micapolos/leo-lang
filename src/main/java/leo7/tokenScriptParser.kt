package leo7

import leo.base.Stack
import leo.base.notNullIf
import leo.base.push

data class TokenScriptParser(
	val scriptBeginStackOrNull: Stack<ScriptBegin>?,
	val script: Script)

val newTokenScriptParser get() = TokenScriptParser(null, script())

fun TokenScriptParser.read(token: Token) =
	when (token) {
		is BeginToken -> copy(
			scriptBeginStackOrNull = scriptBeginStackOrNull.push(script.begin(token.begin.word)),
			script = script())
		is EndToken -> scriptBeginStackOrNull?.let { scriptBeginStack ->
			copy(
				scriptBeginStackOrNull = scriptBeginStack.tail,
				script = scriptBeginStack.head.script.plus(scriptBeginStack.head.begin.word lineTo script))
		}
	}

val TokenScriptParser.parsedScriptOrNull
	get() =
		notNullIf(scriptBeginStackOrNull == null) { script }

