package leo14.untyped.typed

import leo.base.reverseStack
import leo13.fold

data class Exported(val scope: Scope)
data class Library(val scope: Scope, val exported: Exported)

val Scope.exported get() = Exported(this)

fun Scope.library(exported: Exported) = Library(this, exported)

val Scope.emptyLibrary get() = library(emptyScope.exported)

fun Exported.plus(definition: Definition): Exported =
	scope.plus(definition).exported

fun Library.plus(definition: Definition) =
	scope.plus(definition).library(exported.plus(definition))

fun Library.import(definition: Definition) =
	scope.plus(definition).library(exported)

fun Library.import(library: Library): Library =
	fold(library.exported.scope.definitionSeq.reverseStack) { import(it) }
