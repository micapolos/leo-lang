package leo15.type

import leo14.*
import leo15.string

sealed class Scope {
	override fun toString() = reflectScriptLine.string
}

object EmptyScope : Scope()

data class LinkScope(val link: ScopeLink) : Scope() {
	override fun toString() = super.toString()
}

data class ScopeLink(val lhs: Scope, val binding: Binding)

val emptyScope: Scope = EmptyScope
fun Scope.plus(binding: Binding): Scope = LinkScope(ScopeLink(this, binding))
val Scope.linkOrNull: ScopeLink? get() = (this as? LinkScope)?.link

val Scope.reflectScriptLine: ScriptLine
	get() =
		"scope" lineTo reflectScript

val Scope.reflectScript: Script
	get() =
		when (this) {
			EmptyScope -> script()
			is LinkScope -> link.lhs.reflectScript.plus(link.binding.reflectScriptLine)
		}
