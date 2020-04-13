package leo14.untyped.typed.lambda

import leo.base.*
import leo.stak.reverseStack
import leo.stak.seq
import leo13.fold
import leo13.givenName
import leo14.*
import leo14.lambda2.at
import leo14.lambda2.fn
import leo14.lambda2.invoke
import leo14.untyped.doesName
import leo14.untyped.isName
import leo14.untyped.leoString
import leo14.untyped.typed.lineTo
import leo14.untyped.typed.type

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
		?: plusIs(field)
		?: plusDoes(field)
		?: plusField(field)

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

fun Compiler.plusDoes(field: ScriptField): Compiler? =
	ifOrNull(field.string == doesName) {
		typed.staticTypeOrNull?.let { type ->
			library
				.plus(script(givenName) bindingTo type(givenName lineTo type).typed(at(0)))
				.clearLocal
				.applyCompiler(emptyTyped)
				.plus(field.rhs)
				.compiled
				.let { compiled ->
					library
						.plus(type bindingTo compiled.copy(scope = compiled.scope.unsafePop))
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
					.takeOrNull(library.localBindingCount)
					.reverse) { invoke(it!!) })

val Compiler.compiled: Compiled
	get() =
		compiledTyped.run { library.scope.compiled(typed) }

val Compiler.evaluate: Compiler
	get() =
		set(compiledTyped.eval)