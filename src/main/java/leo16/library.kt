package leo16

import leo15.libraryName
import leo15.privateName
import leo15.publicName

data class Library(val privateScope: Scope, val publicScope: Scope) {
	override fun toString() = asSentence.toString()
}

fun Scope.libraryWithPublic(scope: Scope) = Library(this, scope)
val Scope.emptyLibrary get() = libraryWithPublic(emptyScope)
val emptyLibrary get() = emptyScope.emptyLibrary

val Library.asSentence: Sentence
	get() =
		libraryName(
			privateName(privateScope.asSentence),
			publicName(publicScope.asSentence))
