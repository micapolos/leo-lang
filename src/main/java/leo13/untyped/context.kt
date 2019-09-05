package leo13.untyped

import leo.base.fold
import leo13.LeoStruct
import leo13.script.Script
import leo13.script.lineSeq
import leo13.script.linkOrNull

data class context(
	val parentOrNull: context? = null,
	val functions: functions = functions(),
	val bindings: bindings = bindings()) : LeoStruct("context", functions, bindings) {
	override fun toString() = super.toString()
}

fun context.withParent(parent: context) = copy(parentOrNull = parent)
fun context.plus(function: function) = context(null, functions.plus(function), bindings)
fun context.plus(binding: binding) = context(null, functions, bindings.plus(binding))
fun context.evaluate(script: Script) =
	evaluator(this).fold(script.lineSeq) { plus(it) }.evaluated.script

fun context.resolveDefineOrNull(script: Script): context? =
	script
		.linkOrNull
		?.let { link ->
			when (link.line.name) {
				"gives" -> plus(function(pattern(parent.evaluate(link.lhs)), body(link.line.rhs)))
				else -> null
			}
		}

val context.parent: context get() = parentOrNull ?: context()
