package leo21.token.script

import leo14.BeginToken
import leo14.EndToken
import leo14.LiteralToken
import leo14.Script
import leo14.ScriptLine
import leo14.Token
import leo14.line
import leo14.plus
import leo14.script
import leo21.token.processor.ScriptCompilerProcessor
import leo21.token.processor.Processor

data class ScriptCompiler(
	val parentOrNull: ScriptParent?,
	val script: Script
)

val emptyScriptCompiler = ScriptCompiler(null, script())

fun ScriptCompiler.plus(token: Token): Processor =
	when (token) {
		is LiteralToken -> ScriptCompilerProcessor(plus(line(token.literal)))
		is BeginToken -> ScriptCompilerProcessor(
			ScriptCompiler(
				ScriptCompilerNameScriptParent(this, token.begin.string),
				script()))
		is EndToken -> parentOrNull!!.plus(script)
	}

fun ScriptCompiler.plus(line: ScriptLine): ScriptCompiler =
	copy(script = script.plus(line))