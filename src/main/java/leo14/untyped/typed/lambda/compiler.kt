package leo14.untyped.typed.lambda

import leo.base.*
import leo.stak.reverseStack
import leo.stak.seq
import leo13.fold
import leo14.*
import leo14.lambda2.at
import leo14.lambda2.fn
import leo14.lambda2.invoke
import leo14.untyped.*

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

fun Compiler.define(entry: Binding): Compiler =
	library.plus(entry).compiler(emptyTyped)

val Compiler.end: Typed
	get() =
		typed.updateTerm {
			fold(library.scope.bindingStak.reverseStack) {
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
		?: plusGive(field)
		?: plusIs(field)
		?: plusBecomes(field)
		?: plusDoes(field)
		?: plusField(field)

fun Compiler.plusGive(field: ScriptField): Compiler? =
	ifOrNull(field.string == giveName) {
		library
			.plus(script(givenName) bindingTo typed(givenName lineTo typed.type.typed(at(0))))
			.clearLocal
			.applyCompiler(emptyTyped)
			.plus(field.rhs)
			.compiledTyped
			.let { giveTyped -> library.compiler(giveTyped.type.typed(fn(giveTyped.term).invoke(typed.term))) }
	}

fun Compiler.plusIs(field: ScriptField): Compiler? =
	ifOrNull(field.string == isName) {
		typed.staticScriptOrNull?.let { script ->
			library
				.clearLocal
				.applyCompiler(emptyTyped)
				.plus(field.rhs)
				.compiledTyped
				.let { typed -> library.plus(script bindingTo typed).compiler(emptyTyped) }
		}
	}

fun Compiler.plusBecomes(field: ScriptField): Compiler? =
	ifOrNull(field.string == becomesName) {
		library
			.clearLocal
			.applyCompiler(emptyTyped)
			.plus(field.rhs)
			.compiledTyped
			.staticScriptOrNull?.let { script ->
				library.plus(script bindingTo typed).compiler(emptyTyped)
			}
	}

fun Compiler.plusDoes(field: ScriptField): Compiler? =
	ifOrNull(field.string == doesName) {
		typed.staticTypeOrNull?.let { type ->
			library
				.clearLocal
				.applyCompiler(type.typed(at(0)))
				.plus(field.rhs)
				.compiled
				.let { compiled ->
					library
						.plus(type bindingTo compiled.scope.compiled(compiled.typed.withFnTerm))
						.compiler(emptyTyped)
				}
		}
	}

fun Compiler.plusField(field: ScriptField): Compiler =
	plus(field.string lineTo library.clearLocal.applyCompiler(emptyTyped).plus(field.rhs).compiledTyped)

fun Compiler.plus(line: TypedLine): Compiler =
	library.applyCompiler(typed.plus(line))

fun Compiler.set(typed: Typed): Compiler =
	copy(typed = typed)

val Compiler.compiledTyped: Typed
	get() =
		typed.type.typed(
			typed.term
				.iterate(library.localBindingCount) { fn(this) }
				.fold(library.scope.bindingStak.seq
					.map { typed.term }
					.takeOrNull(library.localBindingCount)) { invoke(it!!) })

val Compiler.compiled: Compiled
	get() =
		library.scope.iterate(library.localBindingCount) { pop!! }.compiled(compiledTyped)

val Compiler.evaluate: Compiler
	get() =
		set(compiledTyped.eval)