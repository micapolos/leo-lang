package leo15.type

import leo.base.foldRight
import leo14.*
import leo15.doesName
import leo15.expandsName
import leo15.string

data class Compiler(val library: Library, val typed: Typed) {
	override fun toString() = reflectScriptLine.string
}

val Compiler.reflectScriptLine: ScriptLine
	get() =
		"compiler" lineTo script(library.reflectScriptLine, typed.reflectScriptLine)

fun Library.compiler(typed: Typed) = Compiler(this, typed)
val emptyCompiler = emptyLibrary.compiler(emptyTyped)

fun Compiler.compile(script: Script): Compiler =
	foldRight(script.lineSeq) { compile(it) }

fun Compiler.compile(scriptLine: ScriptLine): Compiler =
	when (scriptLine) {
		is LiteralScriptLine -> compile(scriptLine.literal)
		is FieldScriptLine -> compile(scriptLine.field)
	}

fun Compiler.compile(literal: Literal): Compiler =
	TODO()

fun Compiler.compile(field: ScriptField): Compiler =
	when (field.string) {
		doesName -> compileDoes(field.rhs)
		expandsName -> compileExpands(field.rhs)
		else -> compilePlain(field)
	}

fun Compiler.compileDoes(script: Script): Compiler =
	TODO()

fun Compiler.compileExpands(script: Script): Compiler =
	TODO()

fun Compiler.compilePlain(field: ScriptField): Compiler =
	typed
		.plus(field.string.lineTo(compileTypedRhs(field.rhs)).choice)
		.let { typed ->
			library.applyCompiler(typed)
		}

fun Compiler.set(typed: Typed): Compiler =
	copy(typed = typed)

val Compiler.startLocal: Compiler
	get() =
		library.clearLocal.compiler(emptyTyped)

fun Compiler.compileTypedRhs(script: Script): Typed =
	startLocal.compile(script).scoped.typed

val Compiler.scoped: Scoped
	get() =
		library.scoped(typed)