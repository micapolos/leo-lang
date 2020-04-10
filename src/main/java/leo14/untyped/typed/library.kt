package leo14.untyped.typed

import leo.base.reverseStack
import leo13.fold
import leo14.Begin

data class Exported(val scope: Scope)
data class Library(val scope: Scope, val exported: Exported)

val Scope.exported get() = Exported(this)

fun Scope.library(exported: Exported) = Library(this, exported)

val emptyLibrary get() = emptyScope.library(emptyScope.exported)

fun Exported.plus(definition: Definition): Exported =
	scope.plus(definition).exported

fun Library.plus(definition: Definition) =
	scope.plus(definition).library(exported.plus(definition))

fun Library.import(definition: Definition) =
	scope.plus(definition).library(exported)

fun Library.import(library: Library): Library =
	fold(library.exported.scope.definitionSeq.reverseStack) { import(it) }

fun Library.apply(lhs: Compiled, begin: Begin, rhs: Compiled): Compiled =
	scope.apply(lhs, begin, rhs)

fun Library.applyDefinition(compiled: Compiled): Library? =
	compiled.definitionOrNull(scope)?.let { plus(it) }

fun Library.applyCompiled(compiled: Compiled): Compiled? =
	scope.apply(compiled)