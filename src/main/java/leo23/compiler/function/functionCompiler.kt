package leo23.compiler.function

import leo14.FieldScriptLine
import leo14.LiteralScriptLine
import leo14.ScriptField
import leo14.ScriptLine
import leo21.token.body.Body
import leo23.compiler.Context
import leo23.type.Type
import leo23.type.Types

data class FunctionCompiler(
	val context: Context,
	val types: Types,
	val compiledOrNull: Body?
)

fun FunctionCompiler.plus(scriptLine: ScriptLine): FunctionCompiler =
	if (compiledOrNull != null) null!!
	else when (scriptLine) {
		is LiteralScriptLine -> null!!
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun FunctionCompiler.plus(field: ScriptField): FunctionCompiler = TODO()