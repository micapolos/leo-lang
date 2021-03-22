package leo23.compiler.type

import leo.base.fold
import leo.base.reverse
import leo13.array
import leo13.push
import leo13.stack
import leo14.FieldScriptLine
import leo14.LiteralScriptLine
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.lineSeq
import leo23.type.Type
import leo23.type.Types
import leo23.type.choice
import leo23.type.fields
import leo23.type.struct

data class TypeCompiler(
	val typeBindings: TypeBindings,
	val types: Types
)

fun TypeCompiler.plus(script: Script): TypeCompiler =
	fold(script.lineSeq.reverse) { plus(it) }

fun TypeCompiler.plus(scriptLine: ScriptLine): TypeCompiler =
	when (scriptLine) {
		is LiteralScriptLine -> null!!
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun TypeCompiler.plus(field: ScriptField): TypeCompiler =
	when (field.string) {
		"choice" -> plus(field.string choice fields(*TypeCompiler(typeBindings, stack()).plus(field.rhs).types.array))
		else -> plus(field.string struct fields(*TypeCompiler(typeBindings, stack()).plus(field.rhs).types.array))
	}

fun TypeCompiler.plus(type: Type): TypeCompiler =
	copy(types = typeBindings.resolve(types.push(type)))