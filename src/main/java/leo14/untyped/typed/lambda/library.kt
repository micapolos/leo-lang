package leo14.untyped.typed.lambda

import leo.stak.reverseStack
import leo13.fold
import leo14.ScriptLine
import leo14.lineTo
import leo14.script
import leo14.untyped.leoString

data class Exported(val scope: Scope) {
	override fun toString() = reflectScriptLine.leoString
}

data class Library(val scope: Scope, val exported: Exported) {
	override fun toString() = reflectScriptLine.leoString
}

val Scope.exported get() = Exported(this)

fun Scope.library(exported: Exported) = Library(this, exported)

val emptyLibrary get() = emptyScope.library(emptyScope.exported)

val Library.reflectScriptLine: ScriptLine
	get() =
		"library" lineTo script(scope.reflectScriptLine, exported.reflectScriptLine)
val Exported.reflectScriptLine: ScriptLine
	get() =
		"exported" lineTo script(scope.reflectScriptLine)

fun Exported.plus(entry: Entry): Exported =
	scope.plus(entry).exported

val Library.clearExported: Library get() = scope.library(emptyScope.exported)

fun Library.plus(entry: Entry) =
	scope.plus(entry).library(exported.plus(entry))

fun Library.import(entry: Entry) =
	scope.plus(entry).library(exported)

fun Library.import(library: Library): Library =
	fold(library.exported.scope.entryStak.reverseStack) { import(it) }

fun Library.apply(typed: Typed): Library? =
	null // TODO

//fun Library.apply(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
//	scope.apply(lhs, begin, rhs)
//
//fun Library.applyEntry(compiled: Compiled): Library? =
//	compiled.entryOrNull(scope)?.let { plus(it) }
//
//fun Library.applyCompiled(compiled: Compiled): Compiled? =
//	scope.apply(compiled)