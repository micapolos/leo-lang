package leo14.untyped.typed

data class Export(val scope: Scope)
data class Library(val scope: Scope, val export: Export)

val Scope.export get() = Export(this)
fun Scope.library(export: Export) = Library(this, export)
val Scope.emptyLibrary get() = library(emptyScope.export)