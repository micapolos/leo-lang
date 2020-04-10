package leo14.untyped.typed

import leo.base.reverseStack
import leo13.fold
import leo14.Begin
import leo14.untyped.isName

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

fun Library.apply(compiled: Compiled): Library? =
	null
		?: applyIs(compiled)
		?: applyDoes(compiled)

fun Library.applyIs(compiled: Compiled): Library? =
	compiled.type.matchInfix(isName) { rhs ->
		matchStatic {
			plus(definition(binding(this, rhs.compiled(compiled.linkApply(compiled.type) { it }))))
		}
	}

fun Library.applyDoes(compiled: Compiled): Library? =
	null // TODO()

fun Library.applyCompiled(compiled: Compiled): Compiled? =
	scope.apply(compiled)