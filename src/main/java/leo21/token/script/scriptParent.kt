package leo21.token.script

import leo14.Script
import leo14.lineTo
import leo21.token.processor.ScriptCompilerTokenProcessor
import leo21.token.processor.TokenProcessor

sealed class ScriptParent
data class ScriptCompilerNameScriptParent(val scriptCompiler: ScriptCompiler, val name: String) : ScriptParent()

fun ScriptParent.plus(script: Script): TokenProcessor =
	when (this) {
		is ScriptCompilerNameScriptParent -> ScriptCompilerTokenProcessor(scriptCompiler.plus(name lineTo script))
	}