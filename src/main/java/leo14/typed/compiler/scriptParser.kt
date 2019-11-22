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
	val compiler: Compiler<T>) : ScriptParserParent<T>()

fun <T> ScriptParser<T>.parse(token: Token): Compiler<T> =
	when (token) {
		is LiteralToken -> compiler(plus(line(token.literal)))
		is BeginToken -> compiler(ScriptParser(FieldScriptParserParent(this, token.begin.string), script()))
		is EndToken -> parent.end(script)
	}

fun <T> ScriptParserParent<T>.end(script: Script): Compiler<T> =
	when (this) {
		is FieldScriptParserParent -> compiler(scriptParser.plus(name lineTo script))
		is MakeScriptParserParent -> compiledParser.make(script)
		is CommentScriptParserParent -> compiler
	}

fun <T> ScriptParser<T>.plus(line: ScriptLine) =
	copy(script = script.plus(line))
