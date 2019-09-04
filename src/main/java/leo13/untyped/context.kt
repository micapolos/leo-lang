package leo13.untyped

import leo.base.Empty
import leo.base.fold
import leo13.LeoStruct
import leo13.script.Script
import leo13.script.lineSeq

data class context(val empty: Empty = leo.base.empty) : LeoStruct("context") {
	override fun toString() = super.toString()
}

fun context.evaluate(script: Script): Script =
	evaluator(this).fold(script.lineSeq) { plus(it) }.script
