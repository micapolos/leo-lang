package leo14.untyped

import leo14.*

data class Lazy(val scope: Scope, val script: Script) {
	override fun toString() = reflectScriptLine.toString()
}

fun lazy(scope: Scope, script: Script) = Lazy(scope, script)

fun Scope.asLazy(script: Script) = lazy(this, script)

operator fun Lazy.plus(definition: Definition): Lazy =
	lazy(scope.push(definition), script)

val Lazy.value: Value
	get() =
		scope.eval(script).value

val Lazy.eval: Thunk
	get() =
		scope.eval(script)

val Lazy.printScript
	get() =
		script("lazy" lineTo script)

val Lazy.reflectScriptLine
	get() =
		"lazy"(
			scope.reflectScriptLine,
			script.reflectScriptLine)

val Lazy.recurseRule
	get() =
		rule(
			pattern(
				thunk(
					value(
						"anything" lineTo value(),
						"recurse" lineTo value()))),
			body(thunk(this)))
