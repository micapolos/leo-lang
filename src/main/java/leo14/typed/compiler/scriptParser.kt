package leo14.typed.compiler

import leo14.*

data class ScriptParser<T>(
	val parent: ScriptParserParent<T>,
	val script: Script)

sealed class ScriptParserParent<T>

data class FieldScriptParserParent<T>(
	val scriptParser: ScriptParser<T>,
	val name: String) : ScriptParserParent<T>()

data class MakeScriptParserParent<T>(
	val compiledParser: CompiledParser<T>) : ScriptParserParent<T>()

data class CommentScriptParserParent<T>(
	val leo: Leo<T>) : ScriptParserParent<T>()

fun <T> ScriptParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> leo(plus(line(token.literal)))
		is BeginToken -> leo(ScriptParser(FieldScriptParserParent(this, token.begin.string), script()))
		is EndToken -> parent.end(script)
	}

fun <T> ScriptParserParent<T>.end(script: Script): Leo<T> =
	when (this) {
		is FieldScriptParserParent -> leo(scriptParser.plus(name lineTo script))
		is MakeScriptParserParent -> leo(compiledParser.make(script))
		is CommentScriptParserParent -> leo
	}

fun <T> ScriptParser<T>.plus(line: ScriptLine) =
	copy(script = script.plus(line))
