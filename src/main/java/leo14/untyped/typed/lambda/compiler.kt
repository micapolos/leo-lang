package leo14.untyped.typed.lambda

import leo.base.fold
import leo.base.reverse
import leo.stak.reverseStack
import leo13.fold
import leo14.*
import leo14.lambda2.fn
import leo14.lambda2.invoke
import leo14.untyped.isName

data class Compiler(
	val library: Library,
	val typed: Typed)

fun Library.compiler(typed: Typed): Compiler =
	Compiler(this, typed)

val emptyCompiler = emptyLibrary.compiler(emptyTyped)

fun Library.applyCompiler(typed: Typed): Compiler =
	null
		?: scope.apply(typed)?.let { compiler(it) }
		?: apply(typed)?.let { compiler(emptyTyped) }
		?: compiler(typed.apply)

fun Compiler.define(entry: Entry): Compiler =
	library.plus(entry).compiler(emptyTyped)

val Compiler.end: Typed
	get() =
		typed.updateTerm {
			fold(library.scope.entryStak.reverseStack) {
				fn(this).invoke(it.typed.term)
			}
		}

fun Compiler.plus(script: Script): Compiler =
	fold(script.lineSeq.reverse) { plus(it) }

fun Compiler.plus(line: ScriptLine): Compiler =
	when (line) {
		is LiteralScriptLine -> plus(line.literal)
		is FieldScriptLine -> plus(line.field)
	}

fun Compiler.plus(literal: Literal): Compiler =
	plus(literal.typedLine)

fun Compiler.plus(field: ScriptField): Compiler =
	if (field.isSimple) library.applyCompiler(typed(field.string lineTo typed))
	else plusNormalized(field)

fun Compiler.plusNormalized(field: ScriptField): Compiler =
	when (field.string) {
		isName -> TODO()
		else -> plus(field.string lineTo library.clearExported.applyCompiler(emptyTyped).plus(field.rhs).typed)
	}

fun Compiler.plus(line: TypedLine): Compiler =
	library.applyCompiler(typed.plus(line))

fun Compiler.set(typed: Typed): Compiler =
	copy(typed = typed)
