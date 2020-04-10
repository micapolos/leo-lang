package leo14.untyped.typed.lambda

import leo.stak.reverseStack
import leo13.fold

data class Exported(val scope: Scope)
data class Library(val scope: Scope, val exported: Exported)

val Scope.exported get() = Exported(this)

fun Scope.library(exported: Exported) = Library(this, exported)

val emptyLibrary get() = emptyScope.library(emptyScope.exported)

fun Exported.plus(entry: Entry): Exported =
	scope.plus(entry).exported

fun Library.plus(entry: Entry) =
	scope.plus(entry).library(exported.plus(entry))

fun Library.import(entry: Entry) =
	scope.plus(entry).library(exported)

fun Library.import(library: Library): Library =
	fold(library.exported.scope.entryStak.reverseStack) { import(it) }
//
//fun Library.apply(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
//	scope.apply(lhs, begin, rhs)
//
//fun Library.applyEntry(compiled: Compiled): Library? =
//	compiled.entryOrNull(scope)?.let { plus(it) }
//
//fun Library.applyCompiled(compiled: Compiled): Compiled? =
//	scope.apply(compiled)