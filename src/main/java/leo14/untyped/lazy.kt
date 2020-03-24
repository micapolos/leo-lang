package leo14.untyped

import leo14.Script
import leo14.lineTo
import leo14.script

data class Lazy(val context: Context, val script: Script) {
	override fun toString() = printScript.toString()
}

fun lazy(context: Context, script: Script) = Lazy(context, script)

operator fun Lazy.plus(definition: Definition): Lazy =
	lazy(context.push(definition), script)

val Lazy.value: Value
	get() =
		context.eval(script).value

val Lazy.printScript
	get() =
		script("lazy" lineTo script)