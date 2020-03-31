package leo14.untyped

import leo14.Script

data class Closure(val scope: Scope, val script: Script)

fun Scope.closure(script: Script) = Closure(this, script)
operator fun Closure.plus(definition: Definition) =
	scope.push(definition).closure(script)

val Closure.evaluate get() = scope.evaluate(script)
