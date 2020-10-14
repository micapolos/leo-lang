package leo19.compiler

import leo.base.fold
import leo.base.notNullIf
import leo.base.notNullOrError
import leo.base.reverse
import leo14.Script
import leo14.ScriptField
import leo14.fieldOrNull
import leo14.lineSeq
import leo19.type.Type
import leo19.type.fieldTo
import leo19.type.plus
import leo19.type.struct

data class DefineCompiler(
	val context: Context,
	val type: Type
)

fun DefineCompiler.plus(script: Script): DefineCompiler =
	fold(script.lineSeq.reverse.map { it.fieldOrNull!! }) { plus(it) }

fun DefineCompiler.plus(scriptField: ScriptField): DefineCompiler =
	when (scriptField.string) {
		"is" -> plusIs(scriptField.rhs)
		"gives" -> plusGives(scriptField.rhs)
		else -> plusRaw(scriptField)
	}

fun DefineCompiler.plusIs(script: Script): DefineCompiler =
	DefineCompiler(
		context.defineIs(type, context.typed(script)),
		struct())

fun DefineCompiler.plusGives(script: Script): DefineCompiler =
	DefineCompiler(
		context.defineGives(type, context.typed(script)),
		struct())

fun DefineCompiler.plusRaw(scriptField: ScriptField): DefineCompiler =
	copy(type = type.plus(
		scriptField.string fieldTo
			copy(type = struct()).plus(scriptField.rhs).type))

val DefineCompiler.compiledContext
	get() =
		notNullIf(type == struct()) { context }.notNullOrError("non empty type")