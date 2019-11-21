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

fun <T> ScriptParser<T>.parse(token: Token): Leo<T> =
	when (token) {
		is LiteralToken -> null
		is BeginToken -> leo(ScriptParser(FieldScriptParserParent(this, token.begin.string), script()))
		is EndToken -> parent.end(script)
	} ?: error("$this.parse($token)")

fun <T> ScriptParserParent<T>.end(script: Script): Leo<T> =
	when (this) {
		is FieldScriptParserParent -> leo(scriptParser.plus(name fieldTo script))
		is MakeScriptParserParent -> leo(compiledParser.make(script))
	}

fun <T> ScriptParser<T>.plus(field: ScriptField) =
	copy(script = script.plus(field))
