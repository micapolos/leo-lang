package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.Script
import leo13.script.lineSeq
import leo13.script.linkOrNull

data class Context(
	val functions: Functions,
	val bindings: Bindings) : LeoStruct("context", functions, bindings) {
	override fun toString() = super.toString()
}

fun context() = Context(functions(), bindings())
fun Context.plus(function: Function) = Context(functions.plus(function), bindings)
fun Context.plusFunction(fn: (Context) -> Function) = Context(functions.plus(fn(this)), bindings)
fun Context.plus(binding: Binding) = Context(functions, bindings.plus(binding))
fun Context.evaluate(script: Script) =
	evaluator(this).fold(script.lineSeq) { plus(it) }.evaluated.script

fun Context.resolveDefineOrNull(script: Script): Context? =
	script
		.linkOrNull
		?.let { link ->
			when (link.line.name) {
				"gives" -> plus(function(pattern(link.lhs.normalize), body(this, link.line.rhs)))
				else -> null
			}
		}
