package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.Script
import leo13.script.lineSeq
import leo13.script.linkOrNull

data class context(
	val functions: functions = functions(),
	val bindings: bindings = bindings()) : LeoStruct("context", functions, bindings) {
	override fun toString() = super.toString()
}

fun context.plus(function: function) = context(functions.plus(function), bindings)
fun context.plus(binding: binding) = context(functions, bindings.plus(binding))
fun context.evaluate(script: Script): evaluated =
	evaluator(this).fold(script.lineSeq) { plus(it) }.evaluated

fun context.resolveDefineOrNull(script: Script): context? =
	script
		.linkOrNull
		?.let { link ->
			when (link.line.name) {
				"gives" -> plus(function(pattern(link.lhs.normalize), body(link.line.rhs)))
				else -> null
			}
		}
