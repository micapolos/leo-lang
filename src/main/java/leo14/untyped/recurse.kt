package leo14.untyped

import leo14.Script
import leo14.script

data class Recurse(val action: Action)

fun recurse(action: Action) = Recurse(action)

val Recurse.scopeScript: Script get() = script(recurseName)

fun Recurse.apply(thunk: Thunk): Thunk? =
	thunk.matchPrefix(recurseName) { rhs ->
		action.bindAndApply(rhs)
	}