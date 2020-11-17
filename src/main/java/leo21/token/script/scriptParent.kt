package leo21.token.script

import leo14.Script
import leo14.lineTo
import leo21.token.processor.ScriptCompilerProcessor
import leo21.token.processor.Processor

sealed class ScriptParent
data class ScriptCompilerNameScriptParent(val scriptCompiler: ScriptCompiler, val name: String) : ScriptParent()

fun ScriptParent.plus(script: Script): Processor =
	when (this) {
		is ScriptCompilerNameScriptParent -> ScriptCompilerProcessor(scriptCompiler.plus(name lineTo script))
	}