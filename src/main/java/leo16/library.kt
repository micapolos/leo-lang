package leo16

import leo.base.runIfNotNull
import leo13.fold
import leo13.reverse
import leo15.libraryName
import leo15.publicName

data class Library(val scope: Scope, val publicScope: Scope) {
	override fun toString() = asSentence.toString()
}

fun Scope.libraryWithPublic(scope: Scope) = Library(this, scope)
val Scope.emptyLibrary get() = libraryWithPublic(emptyScope)
val emptyLibrary get() = emptyScope.emptyLibrary
val Library.begin get() = scope.emptyLibrary

val Library.asSentence: Sentence
	get() =
		libraryName(
			scope.asSentence,
			publicName(publicScope.asSentence))

operator fun Library.plus(binding: Binding): Library =
	scope.plus(binding).libraryWithPublic(publicScope.plus(binding))

operator fun Library.plus(scope: Scope) =
	fold(scope.bindingStack.reverse) { plus(it) }

fun Library.applyBinding(value: Value): Library? =
	runIfNotNull(scope.bindingOrNull(value)) { plus(it) }
