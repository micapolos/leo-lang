package leo14.untyped.typed.lambda

import leo14.ScriptLine
import leo14.lineTo
import leo14.literal
import leo14.script
import leo14.untyped.leoString

data class Library(val scope: Scope, val localBindingCount: Int) {
	override fun toString() = reflectScriptLine.leoString
}

fun Scope.libraryWithLocalBindingCount(localBindingCount: Int) = Library(this, localBindingCount)
val Scope.emptyLibrary get() = libraryWithLocalBindingCount(0)
val emptyLibrary get() = emptyScope.emptyLibrary

val Library.reflectScriptLine: ScriptLine
	get() =
		"library" lineTo script(
			scope.reflectScriptLine,
			"local" lineTo script(
				"binding" lineTo script(
					"count" lineTo script(literal(localBindingCount)))))

val Library.clearLocal: Library get() = scope.libraryWithLocalBindingCount(0)

fun Library.plus(binding: Binding) =
	scope.plus(binding).libraryWithLocalBindingCount(localBindingCount.inc())

fun Library.apply(typed: Typed): Library? =
	null // TODO
