package leo7

import leo.base.Stack
import leo.base.notNullIf
import leo.base.push

data class ScriptCursor(
	val scriptBeginStackOrNull: Stack<ScriptBegin>?,
	val script: Script)

val Script.cursor get() = ScriptCursor(null, this)

operator fun ScriptCursor.plus(token: Token): ScriptCursor? = when (token) {
	is BeginToken ->
		ScriptCursor(
			scriptBeginStackOrNull.push(script.begin(token.begin.word)),
			script())
	is EndToken -> scriptBeginStackOrNull?.let { scriptBeginStack ->
		ScriptCursor(
			scriptBeginStack.tail,
			scriptBeginStack.head.script.plus(scriptBeginStack.head.begin.word lineTo script))
	}
}

val ScriptCursor.scriptOrNull
	get() =
		notNullIf(scriptBeginStackOrNull == null) { script }

fun Writer<ScriptCursor>.tokenWriter(scriptCursor: ScriptCursor): Writer<Token> =
	writer { token ->
		scriptCursor.plus(token)?.let(this::tokenWriter)
	}

val Writer<ScriptCursor>.tokenWriter get() = tokenWriter(script().cursor)
