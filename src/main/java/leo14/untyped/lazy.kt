package leo14.untyped

import leo14.*

data class Lazy(val context: Context, val script: Script) {
	override fun toString() = reflectScriptLine.toString()
}

fun lazy(context: Context, script: Script) = Lazy(context, script)

operator fun Lazy.plus(definition: Definition): Lazy =
	lazy(context.push(definition), script)

val Lazy.value: Value
	get() =
		context.eval(script).value

val Lazy.force: Thunk
	get() =
		context.eval(script)

val Lazy.printScript
	get() =
		script("lazy" lineTo script)

val Lazy.reflectScriptLine
	get() =
		"lazy"(
			context.reflectScriptLine,
			script.reflectScriptLine)