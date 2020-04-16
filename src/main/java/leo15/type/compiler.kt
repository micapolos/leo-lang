package leo15.type

import leo.base.foldRight
import leo14.*
import leo15.doesName
import leo15.expandsName
import leo15.isName

data class Compiler(val library: Library, val typed: Typed)

fun Library.compiler(typed: Typed) = Compiler(this, typed)

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
		isName -> compileIs(field.rhs)
		doesName -> compileDoes(field.rhs)
		expandsName -> compileExpands(field.rhs)
		else -> compilePlain(field)
	}

fun Compiler.compileIs(script: Script): Compiler =
	TODO()

fun Compiler.compileDoes(script: Script): Compiler =
	TODO()

fun Compiler.compileExpands(script: Script): Compiler =
	TODO()

fun Compiler.compilePlain(field: ScriptField): Compiler =
	set(typed.plus(field.string.lineTo(compileTypedRhs(field.rhs)).choice))

fun Compiler.set(typed: Typed): Compiler =
	copy(typed = typed)

val Compiler.startLocal: Compiler
	get() =
		library.clearLocal.compiler(emptyTyped)

fun Compiler.compileTypedRhs(script: Script): Typed =
	startLocal.compile(script).compiledTyped

val Compiler.compiledTyped: Typed
	get() =
		TODO()