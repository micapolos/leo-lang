package leo16

import leo.base.runIfNotNull
import leo13.fold
import leo13.reverse
import leo15.exportName
import leo15.scopeName

data class Scope(val dictionary: Dictionary, val exportDictionary: Dictionary) {
	override fun toString() = asField.toString()
}

fun Dictionary.scopeWithPublic(dictionary: Dictionary) = Scope(this, dictionary)
val Dictionary.emptyScope get() = scopeWithPublic(emptyDictionary)
val emptyScope get() = emptyDictionary.emptyScope
val Scope.begin get() = dictionary.emptyScope

val Scope.asField: Field
	get() =
		scopeName(
			dictionary.asField,
			exportName(exportDictionary.asField))

operator fun Scope.plus(definition: Definition): Scope =
	dictionary.plus(definition).scopeWithPublic(exportDictionary.plus(definition))

fun Scope.plus(dictionary: Dictionary) =
	fold(dictionary.definitionStack.reverse) { plus(it) }

fun Scope.import(definition: Definition): Scope =
	dictionary.plus(definition).scopeWithPublic(exportDictionary)

fun Scope.export(definition: Definition): Scope =
	dictionary.scopeWithPublic(exportDictionary.plus(definition))

fun Scope.import(dictionary: Dictionary) =
	fold(dictionary.definitionStack.reverse) { import(it) }

fun Scope.export(dictionary: Dictionary) =
	fold(dictionary.definitionStack.reverse) { export(it) }

fun Scope.applyBinding(value: Value): Scope? =
	runIfNotNull(dictionary.definitionOrNull(value)) { plus(it) }
