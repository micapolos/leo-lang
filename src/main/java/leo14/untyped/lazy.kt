package leo14.untyped

import leo14.*

val lazyErrorOnEval = false

data class Lazy(val scope: Scope, val script: Script) {
	override fun toString() = reflectScriptLine.toString()
}

fun lazy(scope: Scope, script: Script) = Lazy(scope, script)

fun Scope.asLazy(script: Script) = lazy(this, script)

operator fun Lazy.plus(definition: Definition): Lazy =
	lazy(scope.push(definition), script)

val Lazy.value: Value
	get() =
		eval.value

val Lazy.eval: Thunk
	get() =
		if (lazyErrorOnEval) error("eval")
		else scope.eval(script)

val Lazy.printScript
	get() =
		script("lazy" lineTo script)

val Lazy.reflectScriptLine
	get() =
		"lazy"(
			scope.reflectScriptLine,
			script.reflectScriptLine)
