package leo19.decompiler

import leo.base.notNullOrError
import leo13.EmptyStack
import leo13.LinkStack
import leo13.Stack
import leo13.fold
import leo13.isEmpty
import leo13.linkOrNull
import leo13.reverse
import leo14.Script
import leo14.ScriptLine
import leo14.lineTo
import leo14.plus
import leo14.script
import leo19.term.ArrayTerm
import leo19.term.Term
import leo19.term.nullTerm
import leo19.type.ArrowType
import leo19.type.ChoiceType
import leo19.type.Field
import leo19.type.StructType
import leo19.type.isStatic
import leo19.typed.Typed
import leo19.typed.TypedField
import leo19.typed.TypedStruct
import leo19.typed.fieldTo
import leo19.typed.of

data class StructDecompiler(
	val script: Script,
	val remainingFieldStack: Stack<Field>
)

val Typed.script: Script
	get() =
		when (type) {
			is StructType -> term.of(type.struct).script
			is ChoiceType -> TODO()
			is ArrowType -> TODO()
		}

val TypedField.scriptLine: ScriptLine get() = name lineTo typed.script

val TypedStruct.script: Script
	get() =
		StructDecompiler(script(), struct.fieldStack.reverse)
			.fold((term as ArrayTerm).stack) { plus(it) }
			.decompile

fun StructDecompiler.plus(term: Term): StructDecompiler =
	remainingFieldStack.linkOrNull.notNullOrError("no more fields").let { fieldLink ->
		if (fieldLink.value.isStatic)
			StructDecompiler(
				script.plus(fieldLink.value.name.fieldTo(nullTerm.of(fieldLink.value.type)).scriptLine),
				fieldLink.stack).plus(term)
		else
			StructDecompiler(
				script.plus(fieldLink.value.name.fieldTo(term.of(fieldLink.value.type)).scriptLine),
				fieldLink.stack)
	}

val StructDecompiler.dropStatic: StructDecompiler
	get() =
		when (remainingFieldStack) {
			is EmptyStack -> this
			is LinkStack ->
				if (!remainingFieldStack.link.value.isStatic) this
				else StructDecompiler(
					script.plus(remainingFieldStack.link.value.name.fieldTo(nullTerm.of(remainingFieldStack.link.value.type)).scriptLine),
					remainingFieldStack.link.stack).dropStatic
		}

val StructDecompiler.decompile: Script
	get() =
		dropStatic.run {
			if (remainingFieldStack.isEmpty) script
			else error("more fields")
		}