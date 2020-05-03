package leo16

import leo.base.runIfNotNull
import leo13.fold
import leo13.reverse
import leo15.exportName
import leo15.scopeName

data class Scope(val library: Library, val exportLibrary: Library) {
	override fun toString() = asField.toString()
}

fun Library.scopeWithPublic(library: Library) = Scope(this, library)
val Library.emptyScope get() = scopeWithPublic(emptyLibrary)
val emptyScope get() = emptyLibrary.emptyScope
val Scope.begin get() = library.emptyScope

val Scope.asField: Field
	get() =
		scopeName(
			library.asField,
			exportName(exportLibrary.asField))

operator fun Scope.plus(definition: Definition): Scope =
	library.plus(definition).scopeWithPublic(exportLibrary.plus(definition))

fun Scope.import(definition: Definition): Scope =
	library.plus(definition).scopeWithPublic(exportLibrary)

fun Scope.import(library: Library) =
	fold(library.definitionStack.reverse) { import(it) }

fun Scope.applyBinding(value: Value): Scope? =
	runIfNotNull(library.definitionOrNull(value)) { plus(it) }
