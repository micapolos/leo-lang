package leo14.untyped

import leo14.Script

data class Lazy(val context: Context, val script: Script) {
	override fun toString() = script.toString()
}

fun lazy(context: Context, script: Script) = Lazy(context, script)

operator fun Lazy.plus(definition: Definition): Lazy =
	lazy(context.push(definition), script)

val Lazy.program: Program
	get() =
		context.eval(script).program