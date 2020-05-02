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

operator fun Scope.plus(binding: Binding): Scope =
	library.plus(binding).scopeWithPublic(exportLibrary.plus(binding))

fun Scope.import(binding: Binding): Scope =
	library.plus(binding).scopeWithPublic(exportLibrary)

fun Scope.import(library: Library) =
	fold(library.bindingStack.reverse) { import(it) }

fun Scope.applyBinding(value: Value): Scope? =
	runIfNotNull(library.bindingOrNull(value)) { plus(it) }
