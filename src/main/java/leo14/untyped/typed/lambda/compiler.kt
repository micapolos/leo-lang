package leo14.untyped.typed.lambda

import leo.base.*
import leo.stak.reverseStack
import leo.stak.seq
import leo13.fold
import leo14.*
import leo14.lambda2.fn
import leo14.lambda2.freeVariableCount
import leo14.lambda2.invoke
import leo14.untyped.isName
import leo14.untyped.leoString

data class Compiler(val library: Library, val typed: Typed) {
	override fun toString() = reflectScriptLine.leoString
}

fun Library.compiler(typed: Typed): Compiler =
	Compiler(this, typed)

val emptyCompiler = emptyLibrary.compiler(emptyTyped)

val Compiler.reflectScriptLine: ScriptLine
	get() =
		"compiler" lineTo script(library.reflectScriptLine, typed.reflectScriptLine)

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
	null
		?: plusIs(field)
		?: plusField(field)

fun Compiler.plusIs(field: ScriptField): Compiler? =
	ifOrNull(field.string == isName) {
		typed.staticTypeOrNull?.let { type ->
			library.clearExported.applyCompiler(emptyTyped).plus(field.rhs).compiledTyped.let { typed ->
				library.plus(type entryTo typed).compiler(emptyTyped)
			}
		}
	}

fun Compiler.plusField(field: ScriptField): Compiler =
	plus(field.string lineTo library.clearExported.applyCompiler(emptyTyped).plus(field.rhs).compiledTyped)

fun Compiler.plus(line: TypedLine): Compiler =
	library.applyCompiler(typed.plus(line))

fun Compiler.set(typed: Typed): Compiler =
	copy(typed = typed)

val Compiler.compiledTyped: Typed
	get() =
		typed.term.freeVariableCount.let { freeVariableCount ->
			typed.type.typed(
				typed.term
					.iterate(freeVariableCount) { fn(this) }
					.fold(library.scope.entryStak.seq.map { typed.term }.takeOrNull(freeVariableCount).reverse) { invoke(it!!) })
		}

val Compiler.evaluate: Compiler
	get() =
		set(compiledTyped.eval)