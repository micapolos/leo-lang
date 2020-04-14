package leo15

import leo.base.fold
import leo.base.ifOrNull
import leo.base.iterate
import leo.base.reverse
import leo.stak.reverseStack
import leo13.fold
import leo14.*
import leo14.lambda2.at
import leo14.lambda2.fn
import leo14.lambda2.invoke
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
		?: compiler(typed).apply
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
		?: plusGives(field)
		?: plusAs(field)
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

fun Compiler.plusGives(field: ScriptField): Compiler? =
	ifOrNull(field.string == givesName) {
		typed.staticTypeOrNull?.let { type ->
			library
				.plus(script(givenName) bindingTo typed(givenName lineTo type.typed(at(0))))
				.clearLocal
				.applyCompiler(emptyTyped)
				.plus(field.rhs)
				.compiled
				.let { compiled ->
					library
						.plus(type bindingTo library.scope.compiled(compiled.typed.withFnTerm))
						.compiler(emptyTyped)
				}
		}
	}

fun Compiler.plusAs(field: ScriptField): Compiler? =
	ifOrNull(field.string == asName) {
		field.rhs.type.let { type ->
			typed.castTo(type)?.let { castTyped ->
				set(castTyped)
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
		compiled.typed

val Compiler.compiled: Compiled
	get() =
		library.scope.compiled(typed).iterate(library.localBindingCount) {
			scope.pairOrNull!!.let { (tailScope, binding) ->
				tailScope.compiled(typed.updateTerm { fn(this).invoke(binding.typed.term) })
			}
		}

val Compiler.evaluate: Compiler
	get() =
		set(compiledTyped.eval)

val Compiler.apply: Compiler?
	get() =
		null
			?: applyDebug
			?: applyEvaluate

val Compiler.applyDebug: Compiler?
	get() =
		typed
			.matchPrefix("debug") { this }
			?.let { typed ->
				emptyLibrary.compiler(typed(set(typed).reflectScriptLine.typedLine))
			}

val Compiler.applyEvaluate: Compiler?
	get() =
		typed
			.matchPrefix("evaluate") { this }
			?.let { typed ->
				set(library.compiler(typed).evaluate.typed)
			}
