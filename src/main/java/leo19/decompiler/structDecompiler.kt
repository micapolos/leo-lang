package leo19.decompiler

import leo.base.notNullOrError
import leo13.EmptyStack
import leo13.LinkStack
import leo13.Stack
import leo13.isEmpty
import leo13.linkOrNull
import leo14.Script
import leo14.plus
import leo19.term.Term
import leo19.term.nullTerm
import leo19.type.Field
import leo19.type.isStatic
import leo19.typed.fieldTo
import leo19.typed.of

data class StructDecompiler(
	val decompiler: Decompiler,
	val script: Script,
	val remainingFieldStack: Stack<Field>
)

fun StructDecompiler.plus(term: Term): StructDecompiler =
	remainingFieldStack.linkOrNull.notNullOrError("no more fields").let { fieldLink ->
		if (fieldLink.value.isStatic)
			StructDecompiler(
				decompiler,
				script.plus(decompiler.scriptLine(fieldLink.value.name.fieldTo(nullTerm.of(fieldLink.value.type)))),
				fieldLink.stack).plus(term)
		else
			StructDecompiler(
				decompiler,
				script.plus(decompiler.scriptLine(fieldLink.value.name.fieldTo(term.of(fieldLink.value.type)))),
				fieldLink.stack)
	}

val StructDecompiler.dropStatic: StructDecompiler
	get() =
		when (remainingFieldStack) {
			is EmptyStack -> this
			is LinkStack ->
				if (!remainingFieldStack.link.value.isStatic) this
				else StructDecompiler(
					decompiler,
					script.plus(decompiler.scriptLine(remainingFieldStack.link.value.name.fieldTo(nullTerm.of(remainingFieldStack.link.value.type)))),
					remainingFieldStack.link.stack).dropStatic
		}

val StructDecompiler.decompile: Script
	get() =
		dropStatic.run {
			if (remainingFieldStack.isEmpty) script
			else error("more fields")
		}