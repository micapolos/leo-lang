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
import leo14.isEmpty
import leo14.lineSeq
import leo19.type.Arrow
import leo19.typed.Typed
import leo19.typed.TypedField
import leo19.typed.getOrNull
import leo19.typed.make
import leo19.typed.nullTyped
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
	if (scriptField.rhs.isEmpty) plus(scriptField.string)
	else plus(
		TypedField(
			scriptField.string,
			Compiler(context, nullTyped).plus(scriptField.rhs).typed))

fun Compiler.plus(typedField: TypedField): Compiler =
	set(typed.plus(typedField))

fun Compiler.plus(name: String): Compiler =
	null
		?: maybePlusGet(name)
		?: plusMake(name)

fun Compiler.maybePlusGet(name: String): Compiler? =
	typed.getOrNull(name)?.let { set(it) }

fun Compiler.plusMake(name: String): Compiler =
	set(typed.make(name))

fun Compiler.set(typed: Typed) =
	copy(typed = typed)
