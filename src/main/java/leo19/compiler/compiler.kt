package leo19.compiler

import leo.base.fold
import leo.base.reverse
import leo13.Stack
import leo13.stack
import leo14.FieldScriptLine
import leo14.Literal
import leo14.LiteralScriptLine
import leo14.NumberLiteral
import leo14.Script
import leo14.ScriptField
import leo14.ScriptLine
import leo14.StringLiteral
import leo14.lineSeq
import leo19.term.get
import leo19.term.term
import leo19.type.Arrow
import leo19.type.contentOrNull
import leo19.type.indexedFieldOrNull
import leo19.type.structOrNull
import leo19.typed.Typed
import leo19.typed.TypedField
import leo19.typed.fieldTo
import leo19.typed.nullTyped
import leo19.typed.of
import leo19.typed.plus

data class Compiler(
	val context: Stack<Arrow>,
	val typed: Typed
)

val emptyCompiler = Compiler(stack(), nullTyped)

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverse) { plus(it) }

fun Compiler.plus(scriptLine: ScriptLine) =
	when (scriptLine) {
		is LiteralScriptLine -> plus(scriptLine.literal)
		is FieldScriptLine -> plus(scriptLine.field)
	}

fun Compiler.plus(literal: Literal): Compiler =
	when (literal) {
		is StringLiteral -> TODO()
		is NumberLiteral -> TODO()
	}

fun Compiler.plus(scriptField: ScriptField) =
	plus(
		TypedField(
			scriptField.string,
			Compiler(context, nullTyped).plus(scriptField.rhs).typed))

fun Compiler.plus(typedField: TypedField): Compiler =
	copy(typed = typed.plus(typedField))

fun Compiler.plus(name: String): Compiler =
	null
		?: maybePlusGet(name)
		?: plusMake(name)

fun Compiler.maybePlusGet(name: String): Compiler? =
	typed.type.structOrNull?.contentOrNull?.structOrNull?.let { struct ->
		struct.indexedFieldOrNull(name)?.let { indexedField ->
			set(indexedField.value.name.fieldTo(typed.term.get(term(indexedField.index)).of(indexedField.value.type)))
		}
	}

fun Compiler.plusMake(name: String): Compiler =
	set(TypedField(name, typed))

fun Compiler.set(typedField: TypedField) =
	copy(typed = nullTyped.plus(typedField))
